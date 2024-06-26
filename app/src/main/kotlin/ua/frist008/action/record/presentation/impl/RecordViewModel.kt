package ua.frist008.action.record.presentation.impl

import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import ua.frist008.action.record.presentation.base.BaseViewModel
import ua.frist008.action.record.presentation.base.dependency.PresentationDependenciesDelegate
import javax.inject.Inject

@HiltViewModel class RecordViewModel @Inject constructor(
    dependencies: PresentationDependenciesDelegate,
) : BaseViewModel(dependencies) {

    fun onInit(pcId: Long) {
        launch {
            // TODO Connection
        }
    }

    override fun onFailure(cause: Throwable) {
        Timber.e(cause)
    }
}
