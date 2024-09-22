package ua.frist008.action.record.core.presentation.dependency

import kotlinx.coroutines.CoroutineDispatcher
import ua.frist008.action.record.core.di.qualifier.concurrent.Computation
import ua.frist008.action.record.core.ui.component.toast.ToastFlow
import ua.frist008.action.record.core.ui.navigation.Navigator
import javax.inject.Inject

data class PresentationDependencies @Inject constructor(
    @Computation override val backgroundDispatcher: CoroutineDispatcher,
    override val navigator: Navigator,
    override val toastFlow: ToastFlow,
) : PresentationDependenciesDelegate
