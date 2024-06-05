package ua.frist008.action.record.presentation.impl

import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import ua.frist008.action.record.presentation.base.BaseViewModel
import ua.frist008.action.record.presentation.base.dependency.PresentationDependenciesDelegate
import javax.inject.Inject

@HiltViewModel class DevicesViewModel @Inject constructor(
    dependencies: PresentationDependenciesDelegate,
) : BaseViewModel(dependencies) {

    override fun onFailure(cause: Throwable) {
        Timber.e(cause)
    }
}
