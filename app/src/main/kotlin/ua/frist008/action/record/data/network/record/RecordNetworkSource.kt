package ua.frist008.action.record.data.network.record

import kotlinx.coroutines.flow.Flow
import ua.frist008.action.record.data.network.NotConnectedException
import ua.frist008.action.record.data.network.device.entity.DeviceDTO
import ua.frist008.action.record.data.network.record.entity.RecordDTO
import ua.frist008.action.record.features.record.entity.RecordCommand

interface RecordNetworkSource {

    val recordFlow: Flow<RecordDTO?>

    /**
     * Holds a session with the PC: the TCP connection for the commands and the UDP status, until
     * the PC closes the connection or sends nothing for the liveness timeout.
     *
     * @param dto the PC to connect to.
     * @throws java.net.SocketTimeoutException when the PC does not accept the connection in time.
     * @throws java.net.BindException when another app holds the status port.
     */
    suspend fun connect(dto: DeviceDTO)

    /**
     * Sends a command to the PC of the open session.
     *
     * @param command the command to send.
     * @throws NotConnectedException when no session is open.
     * @throws java.net.SocketTimeoutException when the PC does not take the command in time.
     */
    suspend fun sendCommand(command: RecordCommand)
}
