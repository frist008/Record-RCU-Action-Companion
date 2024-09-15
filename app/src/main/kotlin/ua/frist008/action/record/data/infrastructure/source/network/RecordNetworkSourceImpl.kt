package ua.frist008.action.record.data.infrastructure.source.network

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import timber.log.Timber
import ua.frist008.action.record.data.infrastructure.entity.dto.DeviceDTO
import ua.frist008.action.record.data.infrastructure.entity.dto.RecordDTO
import ua.frist008.action.record.data.infrastructure.entity.dto.type.Converter
import ua.frist008.action.record.data.infrastructure.entity.dto.type.RecordDataType
import ua.frist008.action.record.data.source.RecordNetworkSource
import ua.frist008.action.record.di.qualifier.execute.IO
import ua.frist008.action.record.domain.entity.type.RecordCommand
import ua.frist008.action.record.util.common.ByteConst
import ua.frist008.action.record.util.common.NetworkDataConst
import java.io.DataOutputStream
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketTimeoutException
import javax.inject.Inject

class RecordNetworkSourceImpl @Inject constructor(
    @IO private val ioDispatcher: CoroutineDispatcher,
) : RecordNetworkSource {

    private val socketMutex = Mutex()
    private val clientSocketState = MutableStateFlow<Socket?>(null)

    private val _recordFlow = MutableStateFlow(RecordDTO(false))
    override val recordFlow = _recordFlow

    private suspend fun emit(newInstance: (RecordDTO) -> RecordDTO) {
        _recordFlow.emit(newInstance(_recordFlow.value))
    }

    private suspend fun getOrCreateClientSocket(dto: DeviceDTO): Socket = socketMutex.withLock {
        val clientSocket = clientSocketState.value

        if (clientSocket != null && !clientSocket.isClosed) {
            return@withLock clientSocket
        }

        val socket = Socket()

        try {
            val inetSocketAddress = InetSocketAddress(dto.ipStr, dto.port)
            socket.connect(inetSocketAddress, TIMEOUT_CONNECT_SOCKET_MS)
            socket.setSoTimeout(TIMEOUT_REQUEST_SOCKET_MS)
            socket.setTcpNoDelay(true)
        } catch (e: SocketTimeoutException) {
            onSocketTimeoutException(e, dto)
        } catch (t: Throwable) {
            socket.run { close() }
            throw t
        }

        clientSocketState.emit(socket)

        return@withLock socket
    }

    override suspend fun connect(dto: DeviceDTO): Unit = withContext(Dispatchers.IO) {
        getOrCreateClientSocket(dto).use {
            DatagramSocket(SERVER_PORT, SERVER_INET_ADDRESS).use { datagramSocket ->
                datagramSocket.setReuseAddress(true)
                datagramSocket.setSoTimeout(5000)

                try {
                    val datagramBuffer = ByteArray(NetworkDataConst.BYTES_IN_MB)
                    val datagramPacket = DatagramPacket(datagramBuffer, datagramBuffer.size)

                    while (isActive) {
                        datagramSocket.receive(datagramPacket)

                        if (isActive && datagramPacket.length > 0) {
                            updateData(datagramPacket.data)
                        }
                    }
                } catch (e: SocketTimeoutException) {
                    onSocketTimeoutException(e, dto)
                }
            }
        }
    }

    private suspend fun onSocketTimeoutException(e: SocketTimeoutException, dto: DeviceDTO) {
        withContext(Dispatchers.Unconfined) {
            if (isActive) {
                emit { RecordDTO(false) }
                Timber.w(e, "${dto.ipStr}:${dto.port};")
            }
        }
    }

    override suspend fun sendCommand(
        dto: DeviceDTO,
        command: RecordCommand,
    ) = socketMutex.withLock {
        val socket = clientSocketState.value ?: return@withLock

        withContext(ioDispatcher) {
            val dataOutputStream = DataOutputStream(socket.getOutputStream())

            // val inputBuffer = ByteArray(NetworkDataConst.BYTES_IN_MB)
            //
            // socket.getInputStream().use { inputStream ->
            //     var countTimeouts = 0
            //
            //     do {
            //         try {
            //             inputStream.read(inputBuffer)
            //             countTimeouts = -1
            //         } catch (e: SocketTimeoutException) {
            //             Timber.e(e)
            //             if (++countTimeouts >= 5) {
            //                 throw e
            //             }
            //         }
            //     } while (countTimeouts >= 0)
            // }

            dataOutputStream.write(command.commandArr, 0, 1)
        }
    }

    private suspend fun updateData(data: ByteArray) {
        var z: Boolean
        var z3 = false

        val data2And3 = ((data[2].toInt() shl ByteConst.BITS_1) and NetworkDataConst.FF00) or
            (data[3].toInt() and ByteConst.MAX_UBYTE)

        if (data[0] == NetworkDataConst.KEY_0 &&
            data[1] == NetworkDataConst.KEY_1 &&
            data2And3 > Short.MAX_VALUE &&
            data[10].toInt() == 2
        ) {
            val witch = Converter.map(data, 11, 4)

            if (witch <= 0) {
                return
            }

            var i = 15
            val q = ByteArray(NetworkDataConst.BYTES_IN_KB)

            while (i < witch + 11) {
                val dataType = RecordDataType.parse(data[i])
                val typeParse = Converter.map(data, i + 1, 4).toInt()
                val position = i + 5

                when (dataType) {
                    is RecordDataType.RecTooltipDataType -> {
                        val length = if (typeParse > q.size) q.size - 1 else typeParse
                        System.arraycopy(data, position, q, 0, length)
                        q[length] = 0

                        emit { old -> dataType.map(q, old) }
                    }

                    is RecordDataType.EngineDataType -> {
                        emit { old -> dataType.map(Triple(data, position, typeParse), old) }
                    }

                    is RecordDataType.WebDataDataType -> {
                        Timber.d("25 - new")
                        if (_recordFlow.value.webCamType > 0) {
                            val itemA: Int = Converter.map(data, position, 4).toInt()
                            val a4: Int = Converter.map(data, position + 4, 4).toInt()
                            val oPosition: Int = Converter.map(data, position + 8, 4).toInt()
                            val a6: Int = Converter.map(data, position + 12, 4).toInt()
                            val a7: Int = Converter.map(data, position + 16, 4).toInt()
                            val a8: Int = Converter.map(data, position + 20, 4).toInt()
                            val a9: Int = Converter.map(data, position + 24, 4).toInt()

                            if (oPosition < _recordFlow.value.webCamType) {
                                var webCamData =
                                    _recordFlow.value.webCamDataList.getOrNull(oPosition)
                                        ?: continue
                                var dataArrF = webCamData.f
                                var dataArrG = webCamData.g

                                if (dataArrG == null || dataArrF == null || dataArrG.size < a6 * a7 * 2) {
                                    dataArrG = ByteArray(a6 * a7 * 2)
                                    dataArrF = ByteArray(a6 * a7 * 2)
                                    webCamData = webCamData.copy(
                                        g = dataArrG,
                                        f = dataArrF,
                                        a = itemA,
                                    )
                                }

                                if (webCamData.a == 0) {
                                    webCamData = webCamData.copy(a = itemA)
                                }

                                if (webCamData.a <= itemA) {
                                    System.arraycopy(data, position + 28, dataArrF, a4 * 2 * a8, a9)

                                    webCamData =
                                        webCamData.copy(b = oPosition, c = a6, d = a7, f = dataArrF)

                                    if (webCamData.a == itemA) {
                                        webCamData = webCamData.copy(e = webCamData.e + 1)

                                        z = webCamData.e >= ((a6 * a7) / a8) + (-1) || z3

                                        if (z) {
                                            System.arraycopy(
                                                dataArrF,
                                                0,
                                                dataArrG,
                                                0,
                                                dataArrF.size,
                                            )
                                            webCamData = webCamData.copy(g = dataArrG)
                                        }
                                    } else {
                                        webCamData = webCamData.copy(e = 0)
                                        z = z3
                                    }

                                    emit { old ->
                                        val newO = old.webCamDataList.toMutableList()
                                        newO[oPosition] = webCamData.copy(a = itemA)
                                        old.copy(webCamDataList = newO)
                                    }

                                    z3 = z
                                }
                            }
                        }
                    }

                    else -> emit { old ->
                        @Suppress("UNCHECKED_CAST")
                        val longDataType = dataType as RecordDataType<Long>
                        longDataType.map(Converter.map(data, position, typeParse), old)
                    }
                }
                i += typeParse + 5
                Timber.v("i2 - $i")
            }
        }
    }

    companion object {

        private const val TIMEOUT_CONNECT_SOCKET_MS = 3000
        private const val TIMEOUT_REQUEST_SOCKET_MS = 1500

        private const val SERVER_PORT = 4197
        private val SERVER_INET_ADDRESS = InetAddress.getByName("0.0.0.0")
    }
}
