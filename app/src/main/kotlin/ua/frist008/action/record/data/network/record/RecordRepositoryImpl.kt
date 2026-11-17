package ua.frist008.action.record.data.network.record

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import ua.frist008.action.record.data.local.device.DeviceDAO
import ua.frist008.action.record.data.network.DisconnectException
import ua.frist008.action.record.data.network.record.entity.RecordDTO
import ua.frist008.action.record.features.record.RecordRepository
import ua.frist008.action.record.features.record.entity.RecordCommand
import ua.frist008.action.record.features.record.entity.RecordDomainEntity
import javax.inject.Inject

class RecordRepositoryImpl @Inject constructor(
    private val deviceDAO: DeviceDAO,
    private val recordNetworkSource: RecordNetworkSource,
) : RecordRepository {

    override val recordFlow: Flow<RecordDomainEntity>
        get() = recordNetworkSource.recordFlow.filterNotNull().map(RecordDTO::toDomain)

    override suspend fun connect() {
        val dbo = deviceDAO.get() ?: throw DisconnectException()

        recordNetworkSource.connect(dbo.toDTO())
    }

    override suspend fun sendCommand(command: RecordCommand) =
        recordNetworkSource.sendCommand(command)
}
