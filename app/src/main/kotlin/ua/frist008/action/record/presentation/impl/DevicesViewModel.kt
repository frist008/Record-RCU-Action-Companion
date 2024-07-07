package ua.frist008.action.record.presentation.impl

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.getAndUpdate
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

    private var restartScanWithDelayJob = atomic<Job?>(null)
    private var scanJob = atomic<Job?>(null)

    fun onInit() {
        restartScan()
    }

    // TODO For version 1.2: move work with Repository to ForegroundService
    private fun restartScan() {
        scanJob.getAndUpdate { oldRadarJob ->
            oldRadarJob?.cancel()
            restartScanWithDelayJob.value?.cancel()

            launch {
                mutableState.emit(DeviceLoadingState())
                oldRadarJob?.join()

                Timber.i("start Scan")

                devicesRepository.get().collectLatest { list ->
                    if (list.none { it.isAvailableStatus }) {
                        restartScanWithDelay()
                    } else {
                        restartScanWithDelayJob.value?.cancelAndJoin()
                        if (isActive) mutableState.emit(DevicesSuccessState(list.toUI()))
                    }
                }
            }
        }
    }

    private fun restartScanWithDelay() {
        restartScanWithDelayJob.getAndUpdate {
            if (it?.isActive == true) return

            launch {
                timer(
                    durationMs = TIMER_MS,
                    delayEventMs = 1.seconds.inWholeMilliseconds,
                    onNextEvent = { durationLeft ->
                        mutableState.emit(DeviceLoadingState(durationLeft.inWholeSeconds.toString()))
                    },
                    onFinished = ::restartScan,
                )
            }
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
            restartScanWithDelayJob.value?.cancel()
            restartScan()
        }
    }

    fun onDispose() {
        scanJob.value?.cancel()
        restartScanWithDelayJob.value?.cancel()
    }

    companion object {

        private const val TIMER_MS = 5000L
    }
}
