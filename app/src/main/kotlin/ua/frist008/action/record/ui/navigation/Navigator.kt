package ua.frist008.action.record.ui.navigation

import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * Inject on [ua.frist008.action.record.di.module.ui.NavigationModule]
 */
class Navigator(
    private val navigatorSharedFlow: MutableSharedFlow<NavCommand> = MutableSharedFlow(),
) : MutableSharedFlow<NavCommand> by navigatorSharedFlow
