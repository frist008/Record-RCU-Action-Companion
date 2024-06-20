package ua.frist008.action.record.data.infrastructure.source.network

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.TickerMode
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.internal.closeQuietly
import timber.log.Timber
import ua.frist008.action.record.data.infrastructure.entity.dto.DeviceDTO
import ua.frist008.action.record.data.infrastructure.entity.dto.RadarRequestDataHolder
import ua.frist008.action.record.di.qualifier.execute.IO
import ua.frist008.action.record.util.extension.concurrent.onCancel
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.net.SocketTimeoutException
import javax.inject.Inject
import kotlin.math.pow

class DeviceRadarNetworkSource @Inject constructor(
    @IO private val ioDispatcher: CoroutineDispatcher,
) {

    private val socketMutex = Mutex()
    private val packetMutex = Mutex()

    private val datagramSocketState = MutableStateFlow<DatagramSocket?>(null)

    suspend fun get(): Flow<DeviceDTO?> {
        val requestDataHolder: RadarRequestDataHolder = RadarRequestDataHolder.newInstance()
        val radarDatagramSocket = getOrCreateDatagramSocket()
        val clientDatagramPacketList = getClientDatagramPacketList(requestDataHolder)

        return ticker(
            delayMillis = TIMEOUT_MS,
            context = ioDispatcher,
            mode = TickerMode.FIXED_DELAY,
        )
            .receiveAsFlow()
            .map { get(clientDatagramPacketList, radarDatagramSocket, requestDataHolder) }
            .onCancel {
                radarDatagramSocket.closeQuietly()
                Timber.i("cancel")
            }
    }

    private fun get(
        clientDatagramPacketList: List<DatagramPacket>,
        radarDatagramSocket: DatagramSocket,
        requestDataHolder: RadarRequestDataHolder,
    ): DeviceDTO? =
        clientDatagramPacketList
            .asSequence()
            .mapNotNull { requestDatagramPacket -> get(radarDatagramSocket, requestDatagramPacket) }
            .map { responseDatagramPacket -> parse(responseDatagramPacket, requestDataHolder) }
            .firstOrNull()

    private fun get(
        datagramSocket: DatagramSocket,
        clientDatagramPacket: DatagramPacket,
    ): DatagramPacket? {
        return try {
            val responseData = ByteArray(RESPONSE_SIZE)
            val responseDatagramPacket = DatagramPacket(responseData, responseData.size)

            datagramSocket.send(clientDatagramPacket)
            datagramSocket.receive(responseDatagramPacket)

            if (responseDatagramPacket.length > 0) {
                responseDatagramPacket
            } else {
                null
            }
        } catch (e: SocketTimeoutException) {
            Timber.v(e)
            null
        } catch (e: IOException) {
            Timber.e(e)
            null
        }
    }

    private suspend fun getOrCreateDatagramSocket(): DatagramSocket = socketMutex.withLock {
        val radarDatagramSocket = datagramSocketState.value
        if (radarDatagramSocket != null && !radarDatagramSocket.isClosed) {
            return@withLock radarDatagramSocket
        }

        val resultDatagramSocket = DatagramSocket(RESPONSE_PORT, RESPONSE_INET_ADDRESS)

        Timber.i("init socket")
        resultDatagramSocket.setSoTimeout(TIMEOUT_MS.toInt())

        datagramSocketState.emit(resultDatagramSocket)

        return@withLock resultDatagramSocket
    }

    private suspend fun getClientDatagramPacketList(requestDataHolder: RadarRequestDataHolder): List<DatagramPacket> =
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
                    DatagramPacket(dataArr, dataArr.size, REQUEST_INET_ADDRESS, REQUEST_PORT)
                }
                .toList()
        }

    private fun getClientInetAddressSequence(): Sequence<Inet4Address> {
        return try {
            NetworkInterface
                .getNetworkInterfaces()
                .asSequence()
                .map { networkInterface -> networkInterface.inetAddresses.asSequence() }
                .flatten()
                .filter { !it.isLoopbackAddress && it.isSiteLocalAddress }
                .mapNotNull { it as? Inet4Address? }
        } catch (e: SocketException) {
            Timber.e(e)
            sequenceOf()
        }
    }

    private fun parse(
        datagramPacket: DatagramPacket,
        requestDataHolder: RadarRequestDataHolder,
    ): DeviceDTO? {
        val clientDataArr = datagramPacket.data
        val size = datagramPacket.length

        return if (requestDataHolder.isResponseValid(clientDataArr)) { // clientDataArr[0-3]
            val ip = parseIp(clientDataArr) // clientDataArr[4-7]
            val port = parsePort(clientDataArr) // clientDataArr[8-9]
            val namePC =
                if (clientDataArr[10].toInt() == 1) { // clientDataArr[10] is true
                    parseName(clientDataArr, size) // clientDataArr[11-15]
                } else {
                    null
                }

            DeviceDTO(namePC, ip, port)
        } else {
            null
        }
    }

    private fun parseIp(clientDataArr: ByteArray): Long = // clientDataArr[4-7]
        (((clientDataArr[7].toLong()) shl (Byte.SIZE_BITS * 3)) and -MAX_BYTES_POW_3) or
            (((clientDataArr[6].toLong()) shl (Byte.SIZE_BITS * 2)) and FFFF_PLUS_1) or
            (((clientDataArr[5].toLong()) shl Byte.SIZE_BITS) and FF00.toLong()) or
            ((clientDataArr[4].toLong()) and UByte.MAX_VALUE.toLong())

    private fun parsePort(clientDataArr: ByteArray): Int = // clientDataArr[8-9]
        ((clientDataArr[8].toInt() shl Byte.SIZE_BITS) and FF00) or
            (clientDataArr[9].toInt() and UByte.MAX_VALUE.toInt())

    private fun parseName(clientDataArr: ByteArray, size: Int): String? { // clientDataArr[11-15]
        val namePCLong = ((clientDataArr[14].toLong()) and MAX_UBYTE.toLong()) or
            (((clientDataArr[11].toLong()) shl (Byte.SIZE_BITS * 3)) and -MAX_BYTES_POW_3) or
            (((clientDataArr[12].toLong()) shl (Byte.SIZE_BITS * 2)) and FFFF_PLUS_1) or
            (((clientDataArr[13].toLong()) shl Byte.SIZE_BITS) and FF00.toLong())
        val namePCByteSize =
            (if ((15L) + namePCLong <= (size.toLong())) namePCLong else size.toLong()).toInt()
        val namePCByteArr = ByteArray(namePCByteSize + 1)
        namePCByteArr[namePCByteSize] = 0

        return if (namePCLong > 0) {
            System.arraycopy(clientDataArr, 15, namePCByteArr, 0, namePCByteSize)

            runCatching { String(namePCByteArr) }.onFailure(Timber::e).getOrNull()
        } else {
            null
        }
    }

    companion object {

        private val MAX_UBYTE = UByte.MAX_VALUE.toInt() // 255
        private val MAX_BYTES_VALUE = MAX_UBYTE + 1 // 256
        private val MAX_BYTES_POW_3 = MAX_BYTES_VALUE.toDouble().pow(3.0).toLong() // 16 777 216

        private val MAX_USHORT = UShort.MAX_VALUE.toInt() // 65535
        private val MAX_SHORT_VALUE = MAX_USHORT + 1 // 65536

        private val FF00 = MAX_USHORT - MAX_UBYTE // 65280
        private val FFFF_PLUS_1 = MAX_BYTES_POW_3 - MAX_SHORT_VALUE // 16 711 680

        private const val TIMEOUT_MS = 1000L

        private const val RESPONSE_PORT = 4198
        private val RESPONSE_INET_ADDRESS = InetAddress.getByName("0.0.0.0")

        private const val REQUEST_PORT = 4199
        private val REQUEST_INET_ADDRESS = InetAddress.getByName("255.255.255.255")

        private const val RESPONSE_SIZE = 1024
    }
}
