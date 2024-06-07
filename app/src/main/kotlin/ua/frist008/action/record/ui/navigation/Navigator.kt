package ua.frist008.action.record.ui.navigation

import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * Inject on [ua.frist008.action.record.di.module.ui.NavigationModule]
 */
class Navigator(
    private val navigatorSharedFlow: MutableSharedFlow<NavCommand> = MutableSharedFlow(),
) : MutableSharedFlow<NavCommand> by navigatorSharedFlow {

    @Deprecated(
        message = "Unsupported",
        replaceWith = ReplaceWith("emit(value)"),
        level = DeprecationLevel.ERROR,
    ) override fun tryEmit(value: NavCommand): Nothing = throw UnsupportedOperationException()
}
