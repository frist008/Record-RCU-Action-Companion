package ua.frist008.action.record.domain.entity

import ua.frist008.action.record.ui.entity.device.DeviceSuccessState

data class DeviceDomainEntity(
    val id: Long,
    val availableStatus: Boolean,
    val name: String,
    val address: String,
) {

    fun toUI(): DeviceSuccessState =
        DeviceSuccessState(
            id = id,
            availableStatus = availableStatus,
            name = name,
            address = address,
        )
}
