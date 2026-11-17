package ua.frist008.action.record.features.connection.entity

import androidx.compose.runtime.Immutable
import ua.frist008.action.record.core.ui.UIState
import kotlin.time.Duration

@ConsistentCopyVisibility
@Immutable
data class ConnectionProgressState private constructor(
    val timerValue: String,
    private val _isLoading: Boolean = timerValue.isEmpty(),
) : UIState.Progress() {

    val isLoading = _isLoading || timerValue.isEmpty()

    constructor(duration: Duration) : this((duration.inWholeSeconds + 1).toString())

    constructor() : this("")
}
