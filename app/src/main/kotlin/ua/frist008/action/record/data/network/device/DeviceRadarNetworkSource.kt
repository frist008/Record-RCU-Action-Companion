package ua.frist008.action.record.data.network.device

import kotlinx.coroutines.flow.Flow
import ua.frist008.action.record.data.network.device.entity.DeviceDTO

interface DeviceRadarNetworkSource {

    suspend fun get(): Flow<DeviceDTO?>
}
