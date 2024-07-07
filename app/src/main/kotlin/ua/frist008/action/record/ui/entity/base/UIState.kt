package ua.frist008.action.record.ui.entity.base

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable sealed interface UIState {

    open class Success : UIState

    @Immutable open class Progress : UIState

    @Stable open class Error(message: String? = null, cause: Throwable? = null) :
        Exception(message, cause),
        UIState
}
