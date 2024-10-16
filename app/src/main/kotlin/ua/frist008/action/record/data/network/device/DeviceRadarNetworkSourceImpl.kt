package ua.frist008.action.record.data.network.device

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.TickerMode
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import ua.frist008.action.record.core.di.qualifier.concurrent.IO
import ua.frist008.action.record.core.util.concurrent.onCancel
import ua.frist008.action.record.data.mapper.ByteArrayToIpMapper
import ua.frist008.action.record.data.mapper.ByteArrayToNameMapper
import ua.frist008.action.record.data.mapper.ByteArrayToPortMapper
import ua.frist008.action.record.data.network.device.entity.DeviceDTO
import ua.frist008.action.record.data.network.device.entity.RadarRequestDataHolder
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketTimeoutException
import javax.inject.Inject

class DeviceRadarNetworkSourceImpl @Inject constructor(
    @IO private val ioDispatcher: CoroutineDispatcher,
) : DeviceRadarNetworkSource {

    private val socketMutex = Mutex()
    private val packetMutex = Mutex()

    private val radarUdpClientSocketState = MutableStateFlow<DatagramSocket?>(null)

    override suspend fun get(): Flow<DeviceDTO?> {
        val requestDataHolder = RadarRequestDataHolder.newInstance()
        val udpClientSocket = getOrCreateUdpClientSocket()
        val clientDataList = getClientDataList(requestDataHolder)

        return ticker(
            delayMillis = TIMEOUT_PING_MS,
            initialDelayMillis = 0L,
            context = ioDispatcher,
            mode = TickerMode.FIXED_DELAY,
        )
            .receiveAsFlow()
            .map { get(udpClientSocket, clientDataList, requestDataHolder) }
            .onCancel {
                udpClientSocket.close()
                Timber.i("cancel")
            }
    }

    private fun get(
        udpClientSocket: DatagramSocket,
        clientDataList: List<DatagramPacket>,
        requestDataHolder: RadarRequestDataHolder,
    ): DeviceDTO? =
        clientDataList
            .asSequence()
            .mapNotNull { requestData -> get(udpClientSocket, requestData) }
            .map { responseData -> parse(responseData, requestDataHolder) }
            .firstOrNull()

    private fun get(udpClientSocket: DatagramSocket, clientData: DatagramPacket): DatagramPacket? =
        try {
            val responseDataArr = ByteArray(RESPONSE_SIZE)
            val responseData = DatagramPacket(responseDataArr, responseDataArr.size)

            udpClientSocket.send(clientData)
            udpClientSocket.receive(responseData)

            if (responseData.length > 0) {
                responseData
            } else {
                null
            }
        } catch (e: SocketTimeoutException) {
            Timber.v(e)
            null
        } catch (e: IOException) {
            Timber.w(e)
            null
        }

    private suspend fun getOrCreateUdpClientSocket(): DatagramSocket = socketMutex.withLock {
        val radarUdpClientSocket = radarUdpClientSocketState.value
        if (radarUdpClientSocket != null && !radarUdpClientSocket.isClosed) {
            return@withLock radarUdpClientSocket
        }

        val resultUdpClientSocket = DatagramSocket(SERVER_PORT, SERVER_INET_ADDRESS)

        Timber.i("init socket")
        resultUdpClientSocket.setSoTimeout(TIMEOUT_PONG_MS)

        radarUdpClientSocketState.emit(resultUdpClientSocket)

        return@withLock resultUdpClientSocket
    }

    private suspend fun getClientDataList(requestDataHolder: RadarRequestDataHolder): List<DatagramPacket> =
        packetMutex.withLock {
            return@withLock getClientInetAddressSequence()
                .distinctBy { it.hostAddress }
                .map { inetAddress ->
                    val addressArr = inetAddress.address
                    val dataArr = requestDataHolder.newClientDataInstance()
                    dataArr[4] = addressArr[0]
                    dataArr[5] = addressArr[1]
                    dataArr[6] = addressArr[2]
                    dataArr[7] = addressArr[3]
                    DatagramPacket(dataArr, dataArr.size, CLIENT_INET_ADDRESS, CLIENT_PORT)
                }
                .toList()
        }

    private fun getClientInetAddressSequence(): Sequence<Inet4Address> = runCatching {
        NetworkInterface
            .getNetworkInterfaces()
            .asSequence()
            .map { networkInterface -> networkInterface.inetAddresses.asSequence() }
            .flatten()
            .filter { !it.isLoopbackAddress && it.isSiteLocalAddress }
            .mapNotNull { it as? Inet4Address? }
    }.onFailure(Timber::e).getOrElse { emptySequence() }

    private fun parse(
        datagramPacket: DatagramPacket,
        requestDataHolder: RadarRequestDataHolder,
    ): DeviceDTO? {
        val clientDataArr = datagramPacket.data

        return if (requestDataHolder.isResponseValid(clientDataArr)) { // clientDataArr[0-3]
            val ip = ByteArrayToIpMapper(clientDataArr) // clientDataArr[4-7]
            val port = ByteArrayToPortMapper(clientDataArr) // clientDataArr[8-9]
            val namePC =
                if (clientDataArr[10].toInt() == 1) { // clientDataArr[10] is true
                    val size = datagramPacket.length
                    ByteArrayToNameMapper(clientDataArr, size) // clientDataArr[11-15]
                } else {
                    null
                }

            DeviceDTO(namePC, ip, port)
        } else {
            null
        }
    }

    companion object {

        private const val TIMEOUT_PING_MS = 1000L
        private const val TIMEOUT_PONG_MS = 1500

        private const val RESPONSE_SIZE = 1024

        private const val SERVER_PORT = 4198
        private val SERVER_INET_ADDRESS = InetAddress.getByName("0.0.0.0")

        private const val CLIENT_PORT = 4199
        private val CLIENT_INET_ADDRESS = InetAddress.getByName("255.255.255.255")
    }
}
