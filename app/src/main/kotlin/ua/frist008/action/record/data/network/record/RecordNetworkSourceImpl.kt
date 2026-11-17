package ua.frist008.action.record.data.network.record

import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.BoundDatagramSocket
import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.readAvailable
import io.ktor.utils.io.writeFully
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.io.readByteArray
import timber.log.Timber
import ua.frist008.action.record.core.di.qualifier.concurrent.IO
import ua.frist008.action.record.data.mapper.ByteArrayToRecordMapper
import ua.frist008.action.record.data.network.DatagramSocketBinder
import ua.frist008.action.record.data.network.NetworkConst
import ua.frist008.action.record.data.network.NotConnectedException
import ua.frist008.action.record.data.network.WifiNetworkBinder
import ua.frist008.action.record.data.network.device.entity.DeviceDTO
import ua.frist008.action.record.data.network.record.entity.RecordDTO
import ua.frist008.action.record.features.record.entity.RecordCommand
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class RecordNetworkSourceImpl @Inject constructor(
    @param:IO private val ioDispatcher: CoroutineDispatcher,
    private val selectorManager: SelectorManager,
    private val wifiNetworkBinder: WifiNetworkBinder,
) : RecordNetworkSource {

    private val commandMutex = Mutex()
    private val commandChannelState = MutableStateFlow<ByteWriteChannel?>(null)

    private val _recordFlow = MutableStateFlow(RecordDTO(false))
    override val recordFlow = _recordFlow

    override suspend fun connect(dto: DeviceDTO) {
        withContext(ioDispatcher) {
            wifiNetworkBinder.withWifi { holdConnection(dto) }
        }
    }

    private suspend fun holdConnection(dto: DeviceDTO) {
        _recordFlow.emit(RecordDTO(false))

        val tcpSocket = connectTcp(dto)

        try {
            val udpSocket = DatagramSocketBinder.bind(
                selectorManager = selectorManager,
                port = NetworkConst.STATUS_PORT,
                isBroadcast = false,
            )

            try {
                holdSession(tcpSocket, udpSocket)
            } finally {
                udpSocket.close()
            }
        } finally {
            commandChannelState.emit(null)
            tcpSocket.close()
            _recordFlow.emit(RecordDTO(false))
            Timber.i("${dto.ipStr}:${dto.port}; closed")
        }
    }

    private suspend fun connectTcp(dto: DeviceDTO): Socket =
        withTimeoutOrNull(CONNECT_TIMEOUT) {
            aSocket(selectorManager).tcp().connect(dto.ipStr, dto.port) { noDelay = true }
        } ?: throw SocketTimeoutException("${dto.ipStr}:${dto.port}")

    /**
     * Holds the open session until the PC closes the TCP connection or sends nothing, neither over
     * TCP nor over UDP, for the liveness timeout.
     *
     * @param tcpSocket the connection the commands go to and the PC writes to.
     * @param udpSocket the socket the status packets come to.
     */
    private suspend fun holdSession(tcpSocket: Socket, udpSocket: BoundDatagramSocket) =
        coroutineScope {
            val activityCounter = MutableStateFlow(0L)

            commandChannelState.emit(tcpSocket.openWriteChannel())

            val tcpReader = launch { readTcp(tcpSocket.openReadChannel(), activityCounter) }
            val silenceWatcher = launch { awaitSilence(activityCounter) }

            launch { receiveStatus(udpSocket, activityCounter) }

            select {
                tcpReader.onJoin {}
                silenceWatcher.onJoin {}
            }

            coroutineContext.cancelChildren()
        }

    private suspend fun readTcp(channel: ByteReadChannel, activityCounter: MutableStateFlow<Long>) {
        val bufferArr = ByteArray(TCP_BUFFER_SIZE)

        try {
            while (channel.readAvailable(bufferArr, 0, bufferArr.size) >= 0) {
                activityCounter.update { it + 1 }
            }
        } catch (e: IOException) {
            Timber.w(e)
        }
    }

    private suspend fun receiveStatus(
        socket: BoundDatagramSocket,
        activityCounter: MutableStateFlow<Long>,
    ) {
        while (true) {
            val dataArr = socket.receive().packet.readByteArray()
            val oldRecord = _recordFlow.value
            val record = ByteArrayToRecordMapper(dataArr, dataArr.size, oldRecord) ?: continue

            activityCounter.update { it + 1 }
            _recordFlow.emit(record)
        }
    }

    private suspend fun awaitSilence(activityCounter: StateFlow<Long>) {
        while (true) {
            val seenCount = activityCounter.value

            withTimeoutOrNull(LIVENESS_TIMEOUT) {
                activityCounter.firstOrNull { it != seenCount }
            } ?: return
        }
    }

    override suspend fun sendCommand(command: RecordCommand) =
        commandMutex.withLock {
            val channel = commandChannelState.value ?: throw NotConnectedException()

            withContext(ioDispatcher) {
                withTimeoutOrNull(WRITE_TIMEOUT) {
                    channel.writeFully(command.commandArr)
                    channel.flush()
                } ?: throw SocketTimeoutException("$command")
            }
        }

    companion object {

        private val CONNECT_TIMEOUT = 3.seconds
        private val WRITE_TIMEOUT = 1.5.seconds

        /** Silence of the PC that ends the session: five reads of 1.5 s in the original app */
        private val LIVENESS_TIMEOUT = 7.5.seconds

        private const val TCP_BUFFER_SIZE = 1024
    }
}
