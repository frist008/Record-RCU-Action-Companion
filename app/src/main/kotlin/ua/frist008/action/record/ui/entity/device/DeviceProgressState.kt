package ua.frist008.action.record.ui.entity.device

import androidx.compose.runtime.Stable
import ua.frist008.action.record.ui.entity.base.UIState

@Stable data class DeviceProgressState(
    val timerValue: String,
) : UIState.Progress()
