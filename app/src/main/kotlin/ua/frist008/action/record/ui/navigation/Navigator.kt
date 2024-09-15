package ua.frist008.action.record.ui.navigation

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * Inject on [ua.frist008.action.record.di.module.ui.NavigationModule]
 */
@Stable class Navigator(
    sharedFlow: MutableSharedFlow<NavCommand> = MutableSharedFlow(),
) : MutableSharedFlow<NavCommand> by sharedFlow {

    @Deprecated(
        message = "Unsupported",
        replaceWith = ReplaceWith("emit(value)"),
        level = DeprecationLevel.ERROR,
    )
    override fun tryEmit(value: NavCommand): Nothing = throw UnsupportedOperationException()
}
