package ua.frist008.action.record.features.device.entity

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.PersistentList
import ua.frist008.action.record.core.ui.UIState

@Immutable data class DevicesSuccessState(
    val list: PersistentList<DeviceSuccessState>,
) : UIState.Success()

@Immutable data class DeviceSuccessState(
    val id: Long,
    val isAvailableStatus: Boolean,
    val deviceType: DeviceType = if (isAvailableStatus) {
        DeviceType.ONLINE_PC
    } else {
        DeviceType.OFFLINE_PC
    },
    val name: String?,
    val address: String,
) : UIState.Success()
