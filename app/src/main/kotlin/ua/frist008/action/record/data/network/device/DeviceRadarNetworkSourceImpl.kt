package ua.frist008.action.record.data.network.device

import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.BoundDatagramSocket
import io.ktor.network.sockets.Datagram
import io.ktor.network.sockets.InetSocketAddress
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.io.Buffer
import kotlinx.io.readByteArray
import timber.log.Timber
import ua.frist008.action.record.core.di.qualifier.concurrent.IO
import ua.frist008.action.record.data.mapper.ByteArrayToDeviceMapper
import ua.frist008.action.record.data.network.DatagramSocketBinder
import ua.frist008.action.record.data.network.NetworkConst
import ua.frist008.action.record.data.network.device.entity.DeviceDTO
import ua.frist008.action.record.data.network.device.entity.RadarRequestDataHolder
import java.io.IOException
import java.net.Inet4Address
import java.net.NetworkInterface
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

class DeviceRadarNetworkSourceImpl @Inject constructor(
    @param:IO private val ioDispatcher: CoroutineDispatcher,
    private val selectorManager: SelectorManager,
) : DeviceRadarNetworkSource {

    override fun get(): Flow<List<DeviceDTO>> =
        flow {
            val requestDataHolder = RadarRequestDataHolder.newInstance()
            val socket = DatagramSocketBinder.bind(
                selectorManager = selectorManager,
                port = NetworkConst.RADAR_REPLY_PORT,
                isBroadcast = true,
            )

            Timber.i("init socket")

            try {
                scan(socket = socket, requestDataHolder = requestDataHolder, collector = this)
            } finally {
                socket.close()
                Timber.i("cancel")
            }
        }.flowOn(ioDispatcher)

    private suspend fun scan(
        socket: BoundDatagramSocket,
        requestDataHolder: RadarRequestDataHolder,
        collector: FlowCollector<List<DeviceDTO>>,
    ) {
        val requestList = getLocalAddressList()
            .map { address -> requestDataHolder.newClientDataInstance(address.address) }
        var repliedSet = emptySet<DeviceDTO>()

        while (true) {
            requestList.forEach { requestArr -> sendRequest(socket, requestArr) }
            repliedSet = receiveReplies(socket, repliedSet, collector)
            collector.emit(repliedSet.toList())
        }
    }

    /**
     * Receives the replies of one scan period, telling a PC new to the last period at once.
     *
     * @param socket the socket the replies come to.
     * @param previousSet the PCs that replied in the last period.
     * @param collector the collector a new PC is emitted to, together with the PCs already known.
     * @return the PCs that replied in this period.
     */
    private suspend fun receiveReplies(
        socket: BoundDatagramSocket,
        previousSet: Set<DeviceDTO>,
        collector: FlowCollector<List<DeviceDTO>>,
    ): Set<DeviceDTO> {
        val periodMark = TimeSource.Monotonic.markNow()
        val repliedSet = LinkedHashSet<DeviceDTO>()

        while (true) {
            val timeLeft = SCAN_PERIOD - periodMark.elapsedNow()
            val dataArr = withTimeoutOrNull(timeLeft) { socket.receive().packet.readByteArray() }
                ?: return repliedSet
            val dto = ByteArrayToDeviceMapper(dataArr, dataArr.size) ?: continue

            if (repliedSet.add(dto) && dto !in previousSet) {
                collector.emit((previousSet + repliedSet).toList())
            }
        }
    }

    private suspend fun sendRequest(socket: BoundDatagramSocket, requestArr: ByteArray) {
        val packet = Buffer()

        packet.write(requestArr)

        try {
            socket.send(Datagram(packet, REQUEST_ADDRESS))
        } catch (e: IOException) {
            Timber.w(e)
        }
    }

    private fun getLocalAddressList(): List<Inet4Address> = runCatching {
        NetworkInterface
            .getNetworkInterfaces()
            .asSequence()
            .flatMap { networkInterface -> networkInterface.inetAddresses.asSequence() }
            .filter { !it.isLoopbackAddress && it.isSiteLocalAddress }
            .mapNotNull { it as? Inet4Address? }
            .distinctBy { it.hostAddress }
            .toList()
    }.onFailure(Timber::e).getOrElse { emptyList() }

    companion object {

        private val SCAN_PERIOD = 1.seconds

        private val REQUEST_ADDRESS =
            InetSocketAddress(NetworkConst.BROADCAST_HOST, NetworkConst.RADAR_REQUEST_PORT)
    }
}