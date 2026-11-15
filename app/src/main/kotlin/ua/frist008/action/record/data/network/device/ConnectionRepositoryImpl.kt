package ua.frist008.action.record.data.network.device

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ua.frist008.action.record.data.local.device.DeviceDAO
import ua.frist008.action.record.features.connection.ConnectionRepository
import ua.frist008.action.record.features.connection.entity.DeviceDomainEntity
import javax.inject.Inject

class ConnectionRepositoryImpl @Inject constructor(
    private val deviceDAO: DeviceDAO,
    private val deviceRadarNetworkSource: DeviceRadarNetworkSource,
) : ConnectionRepository {

    override fun get(): Flow<DeviceDomainEntity?> =
        deviceRadarNetworkSource.get().map { dtoList ->
            val lastDbo = deviceDAO.get()
            val dto = dtoList.firstOrNull { it.ip == lastDbo?.ip } ?: dtoList.firstOrNull()
            val dbo = dto?.toDBO()

            if (dbo != null && dbo != lastDbo) {
                deviceDAO.insert(dbo)
            }

            dto?.toDomain()
        }
}