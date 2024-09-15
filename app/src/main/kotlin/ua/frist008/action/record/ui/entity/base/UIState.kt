package ua.frist008.action.record.ui.entity.base

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable sealed interface UIState {

    @Stable open class Success : UIState

    @Immutable open class Progress : UIState

    @Immutable open class Error(message: String? = null, cause: Throwable? = null) :
        Exception(message, cause),
        UIState
}
