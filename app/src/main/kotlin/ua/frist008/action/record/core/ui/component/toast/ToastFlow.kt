package ua.frist008.action.record.core.ui.component.toast

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Stable
class ToastFlow(
    sharedFlow: MutableSharedFlow<Int>,
) : MutableSharedFlow<Int> by sharedFlow {

    @Inject constructor() : this(MutableSharedFlow())

    @Deprecated(
        message = "Unsupported",
        replaceWith = ReplaceWith("emit(value)"),
        level = DeprecationLevel.ERROR,
    )
    override fun tryEmit(value: Int): Nothing = throw UnsupportedOperationException()
}
