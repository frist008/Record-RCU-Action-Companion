package ua.frist008.action.record.features.record

import kotlinx.coroutines.flow.Flow
import ua.frist008.action.record.features.record.entity.RecordCommand
import ua.frist008.action.record.features.record.entity.RecordDomainEntity

interface RecordRepository {

    val recordFlow: Flow<RecordDomainEntity>

    /**
     * Holds a session with the PC the phone connected to last until the session ends.
     *
     * @throws ua.frist008.action.record.data.network.DisconnectException when the phone has not
     * connected to a PC yet.
     */
    suspend fun connect()
    suspend fun sendCommand(command: RecordCommand)
}
