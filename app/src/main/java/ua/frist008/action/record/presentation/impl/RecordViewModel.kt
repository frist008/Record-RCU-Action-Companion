package ua.frist008.action.record.presentation.impl

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import ua.frist008.action.record.presentation.base.BaseViewModel
import ua.frist008.action.record.presentation.base.entity.PresentationDependenciesDelegate
import ua.frist008.action.record.ui.entity.RecordUIEntity
import ua.frist008.action.record.ui.entity.base.UIState
import javax.inject.Inject

@HiltViewModel class RecordViewModel @Inject constructor(
    dependencies: PresentationDependenciesDelegate,
) : BaseViewModel<UIState<RecordUIEntity, Exception>>(dependencies) {

    override val mutableState = MutableStateFlow(UIState<RecordUIEntity, Exception>())

    override fun onFailure(cause: Throwable) {
        Timber.e(cause)
    }
}
