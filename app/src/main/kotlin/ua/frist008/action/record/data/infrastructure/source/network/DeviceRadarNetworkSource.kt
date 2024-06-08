package ua.frist008.action.record.data.infrastructure.source.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ua.frist008.action.record.data.infrastructure.entity.dto.DeviceDTO
import javax.inject.Inject

class DeviceRadarNetworkSource @Inject constructor() {

    fun get(): Flow<List<DeviceDTO>> {
        // TODO need to impl
        return MutableStateFlow(
            listOf(
                DeviceDTO(name = "Sample Name 1", ip = "192.168.0.1", mac = "974"),
                DeviceDTO(name = "Sample Name 2", ip = "192.168.0.2", mac = "888"),
            ),
        )
    }
}
