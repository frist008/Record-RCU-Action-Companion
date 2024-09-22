package ua.frist008.action.record.data.network.record

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import timber.log.Timber
import ua.frist008.action.record.data.local.device.DeviceDAO
import ua.frist008.action.record.data.network.DisconnectException
import ua.frist008.action.record.features.record.RecordRepository
import ua.frist008.action.record.features.record.entity.RecordCommand
import ua.frist008.action.record.features.record.entity.RecordDomainEntity
import javax.inject.Inject

class RecordRepositoryImpl @Inject constructor(
    private val databaseSource: DeviceDAO,
    private val recordNetworkSource: RecordNetworkSource,
) : RecordRepository {

    private val currentDeviceId = MutableStateFlow(EMPTY_DEVICE_ID)

    override val recordFlow: Flow<RecordDomainEntity>
        get() = combine(
            currentDeviceId.filter { it != EMPTY_DEVICE_ID },
            recordNetworkSource.recordFlow.filterNotNull(),
        ) { deviceId, dto ->
            dto.toDomain(deviceId)
        }

    override suspend fun connect(deviceId: Long) {
        val dbo = databaseSource.get(deviceId) ?: throw DisconnectException()

        try {
            currentDeviceId.emit(deviceId)
            recordNetworkSource.connect(dbo.toDTO())
        } finally {
            currentDeviceId.tryEmit(EMPTY_DEVICE_ID)
        }
    }

    override suspend fun sendCommand(command: RecordCommand) {
        currentDeviceId
            .filter { it != EMPTY_DEVICE_ID }
            .map { databaseSource.get(it) }
            .firstOrNull()
            ?.let { dbo ->
                recordNetworkSource.sendCommand(dbo.toDTO(), command)
            } ?: Timber.e("$command")
    }

    companion object {

        private const val EMPTY_DEVICE_ID = -1L
    }
}
