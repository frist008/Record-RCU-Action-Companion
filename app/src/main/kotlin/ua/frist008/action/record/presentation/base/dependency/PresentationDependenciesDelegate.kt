package ua.frist008.action.record.presentation.base.dependency

import kotlinx.coroutines.CoroutineDispatcher
import ua.frist008.action.record.ui.navigation.Navigator

interface PresentationDependenciesDelegate {

    val backgroundDispatcher: CoroutineDispatcher

    val navigator: Navigator
}
