package ua.frist008.action.record.core.presentation

import dagger.hilt.android.lifecycle.HiltViewModel
import ua.frist008.action.record.core.presentation.dependency.PresentationDependenciesDelegate
import javax.inject.Inject

@HiltViewModel class RootNavigationViewModel @Inject constructor(
    dependencies: PresentationDependenciesDelegate,
) : BaseViewModel(dependencies)
