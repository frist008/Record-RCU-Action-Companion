package ua.frist008.action.record.features.device.entity

import androidx.compose.runtime.Stable
import ua.frist008.action.record.core.ui.UIState

@Stable data class DeviceLoadingState(
    val timerValue: String = "",
    private val _isLoading: Boolean = timerValue.isEmpty(),
) : UIState.Progress() {

    val isLoading = _isLoading || timerValue.isEmpty()
}
