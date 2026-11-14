package ua.frist008.action.record.data.network.device

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import ua.frist008.action.record.data.local.device.DeviceDAO
import ua.frist008.action.record.data.network.device.entity.DeviceDTO
import ua.frist008.action.record.features.device.DeviceRadarRepository
import ua.frist008.action.record.features.device.entity.DeviceDomainEntity
import javax.inject.Inject

class DeviceRadarRepositoryImpl @Inject constructor(
    private val databaseSource: DeviceDAO,
    private val networkSource: DeviceRadarNetworkSource,
) : DeviceRadarRepository {

    override suspend fun get(): Flow<List<DeviceDomainEntity>> =
        networkSource.get().map { dtoList ->
            dtoList.forEach { dto -> databaseSource.insert(dto.toDBO()) }

            val availableIpSet = dtoList.mapTo(HashSet(), DeviceDTO::ip)
            val actualDboList = databaseSource.getAll().firstOrNull() ?: emptyList()

            actualDboList.map { actual ->
                actual.toDomain(isAvailable = actual.ip in availableIpSet)
            }
        }
}