package ua.frist008.action.record.data.network.record

import kotlinx.coroutines.flow.Flow
import ua.frist008.action.record.data.network.device.entity.DeviceDTO
import ua.frist008.action.record.data.network.record.entity.RecordDTO
import ua.frist008.action.record.features.record.entity.RecordCommand

interface RecordNetworkSource {

    val recordFlow: Flow<RecordDTO?>

    suspend fun connect(dto: DeviceDTO)
    suspend fun sendCommand(dto: DeviceDTO, command: RecordCommand)
}
