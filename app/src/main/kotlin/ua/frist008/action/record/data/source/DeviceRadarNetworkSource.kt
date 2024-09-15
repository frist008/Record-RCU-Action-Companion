package ua.frist008.action.record.data.source

import kotlinx.coroutines.flow.Flow
import ua.frist008.action.record.data.infrastructure.entity.dto.DeviceDTO

interface DeviceRadarNetworkSource {

    suspend fun get(): Flow<DeviceDTO?>
}
