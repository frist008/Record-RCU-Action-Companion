package ua.frist008.action.record.features.device

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.getAndUpdate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import ua.frist008.action.record.core.presentation.BaseViewModel
import ua.frist008.action.record.core.presentation.dependency.PresentationDependenciesDelegate
import ua.frist008.action.record.core.presentation.dependency.StateOwner.Companion.state
import ua.frist008.action.record.core.util.concurrent.timer
import ua.frist008.action.record.features.NavCommand
import ua.frist008.action.record.features.device.entity.DeviceDomainEntity.Companion.toUI
import ua.frist008.action.record.features.device.entity.DeviceLoadingState
import ua.frist008.action.record.features.device.entity.DeviceSuccessState
import ua.frist008.action.record.features.device.entity.DevicesSuccessState
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel class DevicesViewModel @Inject constructor(
    dependencies: PresentationDependenciesDelegate,
    private val devicesRepository: DeviceRadarRepository,
) : BaseViewModel(dependencies) {

    private var restartScanWithDelayJob = atomic<Job?>(null)
    private var scanJob = atomic<Job?>(null)
    private var isAutoFirstNavigate = false

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

                        val uiList = list.toUI()
                        mutableState.emit(DevicesSuccessState(uiList))

                        if (!isAutoFirstNavigate && uiList.size == 1) {
                            isAutoFirstNavigate = true
                            onItemClicked(uiList.first())
                        }
                    }
                }
            }
        }
    }

    private fun restartScanWithDelay() {
        restartScanWithDelayJob.getAndUpdate {
            if (it?.isActive == true) return@getAndUpdate it

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

    override suspend fun onFailure(cause: Throwable) {
        Timber.e(cause)
        restartScanWithDelay()
    }

    fun onItemClicked(device: DeviceSuccessState) {
        launch(Dispatchers.Main.immediate) {
            Timber.i("open RecordScreen")
            navigator.emit(NavCommand.RecordScreen(device.id))
        }
    }

    fun onRefreshClicked(state: DeviceLoadingState = state()) {
        if (!state.isLoading) {
            restartScanWithDelayJob.value?.cancel()
            restartScan()
        }
    }

    fun onLinkCLick() {
        launch(Dispatchers.Main.immediate) {
            navigator.emit(NavCommand.Link("https://steamcommunity.com/sharedfiles/filedetails/?id=3331437683"))
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
