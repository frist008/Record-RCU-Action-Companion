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
import ua.frist008.action.record.data.network.WifiNetworkBinder
import ua.frist008.action.record.data.network.device.entity.DeviceDTO
import ua.frist008.action.record.data.network.device.entity.RadarRequestDataHolder
import java.io.IOException
import java.net.Inet4Address
import java.net.InetAddress
import java.net.InterfaceAddress
import java.net.NetworkInterface
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

class DeviceRadarNetworkSourceImpl @Inject constructor(
    @param:IO private val ioDispatcher: CoroutineDispatcher,
    private val selectorManager: SelectorManager,
    private val wifiNetworkBinder: WifiNetworkBinder,
) : DeviceRadarNetworkSource {

    override fun get(): Flow<List<DeviceDTO>> =
        flow {
            wifiNetworkBinder.withWifi { scanOnSocket(collector = this) }
        }.flowOn(ioDispatcher)

    private suspend fun scanOnSocket(collector: FlowCollector<List<DeviceDTO>>) {
        val requestDataHolder = RadarRequestDataHolder.newInstance()
        val socket = DatagramSocketBinder.bind(
            selectorManager = selectorManager,
            port = NetworkConst.RADAR_REPLY_PORT,
            isBroadcast = true,
        )

        Timber.i("init socket")

        try {
            scan(socket = socket, requestDataHolder = requestDataHolder, collector = collector)
        } finally {
            socket.close()
            Timber.i("cancel")
        }
    }

    private suspend fun scan(
        socket: BoundDatagramSocket,
        requestDataHolder: RadarRequestDataHolder,
        collector: FlowCollector<List<DeviceDTO>>,
    ) {
        val requestList = getLocalInterfaceAddressList().map { interfaceAddress ->
            requestDataHolder.newClientDataInstance(interfaceAddress.address.address) to
                getRequestAddressList(interfaceAddress.broadcast)
        }
        var repliedSet = emptySet<DeviceDTO>()

        while (true) {
            requestList.forEach { (requestArr, addressList) ->
                addressList.forEach { address -> sendRequest(socket, requestArr, address) }
            }
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

    private suspend fun sendRequest(
        socket: BoundDatagramSocket,
        requestArr: ByteArray,
        address: InetSocketAddress,
    ) {
        val packet = Buffer()

        packet.write(requestArr)

        try {
            socket.send(Datagram(packet, address))
        } catch (e: IOException) {
            Timber.w(e, address.toString())
        }
    }

    /**
     * Lists where a request of one local address goes: the broadcast of every network, as the
     * original app sent it, and the broadcast of the address's own subnet, which reaches the PC
     * when the first one leaves through another network.
     *
     * @param subnetBroadcast the broadcast address of the subnet, null for a point-to-point link.
     * @return the addresses the request is sent to.
     */
    private fun getRequestAddressList(subnetBroadcast: InetAddress?): List<InetSocketAddress> {
        val subnetAddress = subnetBroadcast?.hostAddress?.let { host ->
            InetSocketAddress(host, NetworkConst.RADAR_REQUEST_PORT)
        }

        return listOfNotNull(REQUEST_ADDRESS, subnetAddress)
    }

    private fun getLocalInterfaceAddressList(): List<InterfaceAddress> = runCatching {
        NetworkInterface
            .getNetworkInterfaces()
            .asSequence()
            .flatMap { networkInterface -> networkInterface.interfaceAddresses.asSequence() }
            .filter { interfaceAddress ->
                val address = interfaceAddress.address

                address is Inet4Address && !address.isLoopbackAddress && address.isSiteLocalAddress
            }
            .distinctBy { interfaceAddress -> interfaceAddress.address.hostAddress }
            .toList()
    }.onFailure(Timber::e).getOrElse { emptyList() }

    companion object {

        private val SCAN_PERIOD = 1.seconds

        private val REQUEST_ADDRESS =
            InetSocketAddress(NetworkConst.BROADCAST_HOST, NetworkConst.RADAR_REQUEST_PORT)
    }
}
