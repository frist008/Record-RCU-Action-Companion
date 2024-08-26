package ua.frist008.action.record.data.repository

import kotlinx.coroutines.flow.Flow
import ua.frist008.action.record.domain.entity.RecordDomainEntity
import ua.frist008.action.record.domain.entity.type.RecordCommand

interface RecordRepository {

    val recordFlow: Flow<RecordDomainEntity>

    suspend fun connect(deviceId: Long)
    suspend fun sendCommand(command: RecordCommand)
}
