package ua.frist008.action.record.presentation.impl

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import ua.frist008.action.record.data.repository.DeviceRadarRepository
import ua.frist008.action.record.domain.entity.DeviceDomainEntity
import ua.frist008.action.record.presentation.base.BaseViewModel
import ua.frist008.action.record.presentation.base.dependency.PresentationDependenciesDelegate
import ua.frist008.action.record.ui.entity.device.DeviceSuccessState
import ua.frist008.action.record.ui.entity.device.DevicesSuccessState
import ua.frist008.action.record.ui.navigation.NavCommand
import javax.inject.Inject

@HiltViewModel class DevicesViewModel @Inject constructor(
    dependencies: PresentationDependenciesDelegate,
    private val devicesRepository: DeviceRadarRepository,
) : BaseViewModel(dependencies) {

    init {
        // TODO For version 1.2: move work with Repository to ForegroundService
        launch {
            // TODO add error processing
            devicesRepository.get().collectLatest { list ->
                val resultList = list.asSequence().map(DeviceDomainEntity::toUI).toPersistentList()
                // TODO Add timer for loading and error screen
                mutableState.emit(DevicesSuccessState(resultList))
            }
        }
    }

    override fun onFailure(cause: Throwable) {
        Timber.e(cause)
    }

    fun onItemClicked(device: DeviceSuccessState) {
        // TODO will impl after all repository code is completed
        // TODO add error if status offline
        launch(Dispatchers.Main.immediate) {
            navigator.emit(NavCommand.RecordScreen)
        }
    }
}
