package ua.frist008.action.record.features.device

import kotlinx.coroutines.flow.Flow
import ua.frist008.action.record.features.device.entity.DeviceDomainEntity

interface DeviceRadarRepository {

    suspend fun get(): Flow<List<DeviceDomainEntity>>
}
