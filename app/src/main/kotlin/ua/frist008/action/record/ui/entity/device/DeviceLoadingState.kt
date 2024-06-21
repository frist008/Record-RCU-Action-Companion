package ua.frist008.action.record.ui.entity.device

import androidx.compose.runtime.Stable
import ua.frist008.action.record.ui.entity.base.UIState

@Stable data class DeviceLoadingState(
    val timerValue: String = "",
    private val _isLoading: Boolean = timerValue.isEmpty(),
) : UIState.Progress() {

    val isLoading = _isLoading || timerValue.isEmpty()
}
