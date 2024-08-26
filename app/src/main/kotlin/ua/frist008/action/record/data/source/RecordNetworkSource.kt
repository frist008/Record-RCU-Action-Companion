package ua.frist008.action.record.data.source

import kotlinx.coroutines.flow.Flow
import ua.frist008.action.record.data.infrastructure.entity.dto.DeviceDTO
import ua.frist008.action.record.data.infrastructure.entity.dto.RecordDTO
import ua.frist008.action.record.domain.entity.type.RecordCommand

interface RecordNetworkSource {

    val recordFlow: Flow<RecordDTO?>

    suspend fun connect(dto: DeviceDTO)
    suspend fun sendCommand(dto: DeviceDTO, command: RecordCommand)
}
