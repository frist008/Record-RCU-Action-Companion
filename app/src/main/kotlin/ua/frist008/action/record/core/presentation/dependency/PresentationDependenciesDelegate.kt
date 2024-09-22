package ua.frist008.action.record.core.presentation.dependency

import kotlinx.coroutines.CoroutineDispatcher
import ua.frist008.action.record.core.ui.navigation.Navigator

interface PresentationDependenciesDelegate {

    val backgroundDispatcher: CoroutineDispatcher

    val navigator: Navigator
}
