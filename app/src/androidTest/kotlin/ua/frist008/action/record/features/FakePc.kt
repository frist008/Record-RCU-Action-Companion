package ua.frist008.action.record.features

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.frist008.action.record.core.util.io.gb
import ua.frist008.action.record.data.network.NotConnectedException
import ua.frist008.action.record.data.network.device.DeviceRadarNetworkSource
import ua.frist008.action.record.data.network.device.entity.DeviceDTO
import ua.frist008.action.record.data.network.record.RecordNetworkSource
import ua.frist008.action.record.data.network.record.entity.RecordDTO
import ua.frist008.action.record.data.network.record.entity.RecordModeType
import ua.frist008.action.record.features.record.entity.FixedRecordCommand
import ua.frist008.action.record.features.record.entity.RecordCommand
import ua.frist008.action.record.features.record.entity.StreamingRecordCommand
import java.net.SocketTimeoutException
import kotlin.time.Duration.Companion.seconds

/**
 * The PC with Mirillis Action!, faked at the network layer of the app: the search finds it while it
 * is on the network, the session holds until it leaves, and it answers the commands as the recorder
 * does.
 */
class FakePc : DeviceRadarNetworkSource, RecordNetworkSource {

    private val isOnNetworkFlow = MutableStateFlow(false)
    private val statusFlow = MutableStateFlow(DEFAULT_STATUS)
    private val recordMutableFlow = MutableStateFlow<RecordDTO?>(RecordDTO(connected = false))
    private val receivedCommandsFlow = MutableStateFlow<List<RecordCommand>>(emptyList())
    private val connectCountFlow = MutableStateFlow(0)

    /** The commands the app sent, oldest first */
    val receivedCommands: List<RecordCommand> get() = receivedCommandsFlow.value

    /** How many sessions the app opened with the PC */
    val connectCount: Int get() = connectCountFlow.value

    /** What the recorder reports now */
    val status: RecordDTO get() = statusFlow.value

    override val recordFlow: Flow<RecordDTO?> = recordMutableFlow

    fun appear() {
        isOnNetworkFlow.value = true
    }

    fun disappear() {
        isOnNetworkFlow.value = false
    }

    /**
     * Changes what the recorder reports, sent to the app at once while a session is open.
     *
     * @param transform makes the new state from the current one.
     */
    fun updateStatus(transform: (RecordDTO) -> RecordDTO) {
        statusFlow.update(transform)
    }

    override fun get(): Flow<List<DeviceDTO>> =
        isOnNetworkFlow.map { isOnNetwork -> if (isOnNetwork) listOf(DEVICE) else emptyList() }

    override suspend fun connect(dto: DeviceDTO) {
        recordMutableFlow.value = RecordDTO(connected = false)

        if (!isOnNetworkFlow.value) {
            delay(CONNECT_TIMEOUT)
            throw SocketTimeoutException("${dto.ipStr}:${dto.port}")
        }

        connectCountFlow.update { count -> count + 1 }

        try {
            coroutineScope {
                val statusJob = launch {
                    statusFlow.collect { status -> recordMutableFlow.value = status }
                }

                isOnNetworkFlow.first { isOnNetwork -> !isOnNetwork }
                statusJob.cancel()
            }
        } finally {
            recordMutableFlow.value = RecordDTO(connected = false)
        }

        throw SocketTimeoutException("${dto.ipStr}:${dto.port} left the network")
    }

    override suspend fun sendCommand(command: RecordCommand) {
        if (recordMutableFlow.value?.connected != true) throw NotConnectedException()

        receivedCommandsFlow.update { commands -> commands + command }
        statusFlow.update { status -> status.copy(recordModeType = getModeAfter(command)) }
    }

    private fun getModeAfter(command: RecordCommand): RecordModeType =
        when (command) {
            FixedRecordCommand.PAUSE -> RecordModeType.PAUSE

            FixedRecordCommand.STOP,
            StreamingRecordCommand.STOP,
                -> RecordModeType.STOP

            else -> RecordModeType.RECORD
        }

    companion object {

        /** The PC on the network: 192.168.1.10, the port of Action! */
        val DEVICE = DeviceDTO(name = "Gaming PC", ip = 3_232_235_786L, port = 2555)

        /** A recorder that waits with a game open on a disk of 30 GB free */
        val DEFAULT_STATUS = RecordDTO(
            connected = true,
            recordModeType = RecordModeType.STOP,
            fps = 60,
            maxFps = 60,
            engine = "DX12",
            freeSpace = 30.gb,
            gameActive = true,
        )

        private val CONNECT_TIMEOUT = 1.seconds
    }
}