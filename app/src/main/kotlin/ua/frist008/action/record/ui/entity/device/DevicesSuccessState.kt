package ua.frist008.action.record.ui.entity.device

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.PersistentList
import ua.frist008.action.record.ui.entity.base.UIState

@Stable data class DevicesSuccessState(
    val list: PersistentList<DeviceSuccessState>,
) : UIState.Success()
