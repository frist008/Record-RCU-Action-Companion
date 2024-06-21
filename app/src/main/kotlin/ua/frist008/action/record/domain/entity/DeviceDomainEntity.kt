package ua.frist008.action.record.domain.entity

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import ua.frist008.action.record.ui.entity.device.DeviceSuccessState

data class DeviceDomainEntity(
    val id: Long,
    val isAvailableStatus: Boolean,
    val name: String?,
    val address: String,
) {

    fun toUI(): DeviceSuccessState =
        DeviceSuccessState(
            id = id,
            isAvailableStatus = isAvailableStatus,
            name = name,
            address = address,
        )

    companion object {

        fun List<DeviceDomainEntity>.toUI(): PersistentList<DeviceSuccessState> =
            asSequence().map(DeviceDomainEntity::toUI).toPersistentList()
    }
}
