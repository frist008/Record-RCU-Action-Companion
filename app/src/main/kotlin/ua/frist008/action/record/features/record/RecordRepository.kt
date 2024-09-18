package ua.frist008.action.record.features.record

import kotlinx.coroutines.flow.Flow
import ua.frist008.action.record.features.record.entity.RecordCommand
import ua.frist008.action.record.features.record.entity.RecordDomainEntity

interface RecordRepository {

    val recordFlow: Flow<RecordDomainEntity>

    suspend fun connect(deviceId: Long)
    suspend fun sendCommand(command: RecordCommand)
}
