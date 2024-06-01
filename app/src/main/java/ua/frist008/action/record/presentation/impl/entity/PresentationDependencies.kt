package ua.frist008.action.record.presentation.impl.entity

import kotlinx.coroutines.CoroutineDispatcher
import ua.frist008.action.record.di.qualifier.execute.Computation
import ua.frist008.action.record.presentation.base.entity.PresentationDependenciesDelegate
import ua.frist008.action.record.ui.navigation.Navigator
import javax.inject.Inject

data class PresentationDependencies @Inject constructor(
    @Computation override val backgroundDispatcher: CoroutineDispatcher,
    override val navigator: Navigator,
) : PresentationDependenciesDelegate
