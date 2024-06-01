package ua.frist008.action.record.presentation.impl

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import ua.frist008.action.record.presentation.base.BaseViewModel
import ua.frist008.action.record.presentation.base.entity.PresentationDependenciesDelegate
import ua.frist008.action.record.ui.entity.DeviceUIEntity
import ua.frist008.action.record.ui.entity.base.UIState
import javax.inject.Inject

@HiltViewModel class DevicesViewModel @Inject constructor(
    dependencies: PresentationDependenciesDelegate,
) : BaseViewModel<UIState<DeviceUIEntity, Exception>>(dependencies) {

    override val mutableState = MutableStateFlow(UIState<DeviceUIEntity, Exception>())

    override fun onFailure(cause: Throwable) {
        Timber.e(cause)
    }
}
