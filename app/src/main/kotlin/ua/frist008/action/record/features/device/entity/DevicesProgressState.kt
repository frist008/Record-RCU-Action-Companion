package ua.frist008.action.record.features.device.entity

import androidx.compose.runtime.Immutable
import ua.frist008.action.record.core.ui.UIState
import kotlin.time.Duration

@Suppress("DataClassPrivateConstructor")
@Immutable
data class DevicesProgressState private constructor(
    val timerValue: String,
    private val _isLoading: Boolean = timerValue.isEmpty(),
) : UIState.Progress() {

    constructor(duration: Duration) : this((duration.inWholeSeconds + 1).toString())

    constructor() : this("")

    val isLoading = _isLoading || timerValue.isEmpty()
}
