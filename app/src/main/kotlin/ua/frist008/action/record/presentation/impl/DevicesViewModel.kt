package ua.frist008.action.record.presentation.impl

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import timber.log.Timber
import ua.frist008.action.record.data.repository.DeviceRadarRepository
import ua.frist008.action.record.domain.entity.DeviceDomainEntity.Companion.toUI
import ua.frist008.action.record.presentation.base.BaseViewModel
import ua.frist008.action.record.presentation.base.dependency.PresentationDependenciesDelegate
import ua.frist008.action.record.ui.entity.device.DeviceLoadingState
import ua.frist008.action.record.ui.entity.device.DeviceSuccessState
import ua.frist008.action.record.ui.entity.device.DevicesSuccessState
import ua.frist008.action.record.ui.navigation.NavCommand
import ua.frist008.action.record.util.extension.concurrent.timer
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel class DevicesViewModel @Inject constructor(
    dependencies: PresentationDependenciesDelegate,
    private val devicesRepository: DeviceRadarRepository,
) : BaseViewModel(dependencies) {

    @Volatile private var restartScanWithDelayJob: Job? = null
    @Volatile private var scanJob: Job? = null

    fun onInit() {
        restartScan()
    }

    // TODO For version 1.2: move work with Repository to ForegroundService
    private fun restartScan() {
        scanJob?.cancel()
        restartScanWithDelayJob?.cancel()

        val oldRadarJob = scanJob
        scanJob = launch {
            mutableState.emit(DeviceLoadingState())
            oldRadarJob?.join()

            Timber.i("start Scan")

            devicesRepository.get().collectLatest { list ->
                if (list.none { it.isAvailableStatus }) {
                    restartScanWithDelay()
                } else {
                    restartScanWithDelayJob?.cancelAndJoin()
                    if (isActive) mutableState.emit(DevicesSuccessState(list.toUI()))
                }
            }
        }
    }

    private fun restartScanWithDelay() {
        if (restartScanWithDelayJob?.isActive == true) return

        restartScanWithDelayJob = launch {
            timer(
                durationMs = TIMER_MS,
                delayEventMs = 1.seconds.inWholeMilliseconds,
                onNextEvent = {
                    mutableState.emit(DeviceLoadingState(it.inWholeSeconds.toString()))
                },
                onFinished = ::restartScan,
            )
        }
    }

    override fun onFailure(cause: Throwable) {
        Timber.e(cause)
        restartScanWithDelay()
    }

    fun onItemClicked(device: DeviceSuccessState) {
        launch(Dispatchers.Main.immediate) {
            Timber.i("open RecordScreen")
            navigator.emit(NavCommand.RecordScreen(device.id))
        }
    }

    fun onRefreshClicked(state: DeviceLoadingState) {
        if (!state.isLoading) {
            restartScanWithDelayJob?.cancel()
            restartScan()
        }
    }

    fun onDispose() {
        scanJob?.cancel()
        restartScanWithDelayJob?.cancel()
    }

    companion object {

        private const val TIMER_MS = 5000L
    }
}
