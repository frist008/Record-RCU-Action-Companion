package ua.frist008.action.record.features.device.entity

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

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
