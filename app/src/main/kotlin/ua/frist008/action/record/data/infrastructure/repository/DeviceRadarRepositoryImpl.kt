package ua.frist008.action.record.data.infrastructure.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import ua.frist008.action.record.data.infrastructure.entity.dto.DeviceDTO
import ua.frist008.action.record.data.infrastructure.source.database.DeviceDAO
import ua.frist008.action.record.data.infrastructure.source.network.DeviceRadarNetworkSource
import ua.frist008.action.record.data.repository.DeviceRadarRepository
import ua.frist008.action.record.domain.entity.DeviceDomainEntity
import javax.inject.Inject

class DeviceRadarRepositoryImpl @Inject constructor(
    private val databaseSource: DeviceDAO,
    private val networkSource: DeviceRadarNetworkSource,
) : DeviceRadarRepository {

    override fun get(): Flow<List<DeviceDomainEntity>> =
        networkSource.get().map { dtoList ->
            val newDboList = dtoList.map(DeviceDTO::toDBO)
            databaseSource.insert(newDboList)

            val actualDboList = databaseSource.getAll().firstOrNull() ?: listOf()

            actualDboList.map { actual -> actual.toDomain(newDboList.any { it.ip == actual.ip }) }
        }
}
