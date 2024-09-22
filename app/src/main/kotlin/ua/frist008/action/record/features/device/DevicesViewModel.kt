package ua.frist008.action.record.features.device

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import ua.frist008.action.record.core.presentation.BaseViewModel
import ua.frist008.action.record.core.presentation.dependency.PresentationDependenciesDelegate
import ua.frist008.action.record.core.util.concurrent.timer
import ua.frist008.action.record.features.NavCommand
import ua.frist008.action.record.features.device.entity.DeviceDomainEntity.Companion.toUI
import ua.frist008.action.record.features.device.entity.DeviceSuccessState
import ua.frist008.action.record.features.device.entity.DevicesProgressState
import ua.frist008.action.record.features.device.entity.DevicesSuccessState
import java.net.BindException
import java.net.SocketException
import java.net.SocketTimeoutException
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@HiltViewModel class DevicesViewModel @Inject constructor(
    dependencies: PresentationDependenciesDelegate,
    private val devicesRepository: DeviceRadarRepository,
) : BaseViewModel(dependencies) {

    private var ignoreError = false

    private val timerJob = atomic<Job?>(null)
    private val scanJob = atomic<Job?>(null)

    private var isAutoFirstNavigate = false

    fun onInit() {
        restartTimer()
        restartScan()
    }

    // TODO For version 1.2: move work with Repository to ForegroundService
    private fun restartScan() {
        Timber.i("start Scan")

        launch(scanJob) {
            devicesRepository
                .get()
                .catch { onScanError { } }
                .collectLatest { list ->
                    if (list.any { it.isAvailableStatus }) {
                        val uiList = list.toUI()

                        if (!isAutoFirstNavigate && uiList.size == 1) {
                            isAutoFirstNavigate = true
                            navigator.emit(NavCommand.RecordScreen(uiList.first().id))
                        } else {
                            mutableState.emit(DevicesSuccessState(uiList))
                        }

                        ignoreError = true
                    } else {
                        val oldIgnoreError = ignoreError
                        onScanError { if (!oldIgnoreError) restartScan() }
                    }
                }
        }
    }

    private suspend fun onScanError(onAction: () -> Unit) {
        if (ignoreError) {
            ignoreError = false
            Timber.d("Ignore error")
            delay(1.seconds)
        } else if (mutableState.value !is DevicesProgressState) {
            Timber.d("Start Progress")
            mutableState.emit(DevicesProgressState())
        }

        onAction()
    }

    private fun restartTimer(durationDelay: Duration = 0.milliseconds) {
        val emit: suspend (DevicesProgressState) -> Unit = { state ->
            if (mutableState.value is DevicesProgressState) {
                Timber.d("Emit Progress - $state")
                mutableState.emit(state)
            }
        }

        launch(timerJob) {
            delay(durationDelay)
            timer(
                durationMs = TIMER_MS,
                delayEventMs = 1.seconds.inWholeMilliseconds,
                onNextEvent = { durationLeft -> emit(DevicesProgressState(durationLeft)) },
                onFinished = {
                    emit(DevicesProgressState())
                    Timber.d("Restart Progress")
                    restartTimer(2.seconds)
                },
            )
        }
    }

    fun onItemClicked(device: DeviceSuccessState) {
        launch(Dispatchers.Main.immediate) {
            Timber.i("open RecordScreen")
            navigator.emit(NavCommand.RecordScreen(device.id))
        }
    }

    fun onLinkCLick(link: String) {
        launch(Dispatchers.Main.immediate) {
            navigator.emit(NavCommand.Link(link))
        }
    }

    override suspend fun onFailure(cause: Throwable) {
        when (cause) {
            is BindException -> {
                Timber.e(cause)
                // TODO remove on next release
            }

            is SocketTimeoutException,
            is SocketException,
            -> Timber.v(cause)

            else -> super.onFailure(cause)
        }
    }

    fun onDispose() {
        scanJob.value?.cancel()
        timerJob.value?.cancel()
    }

    companion object {

        private const val TIMER_MS = 5000L
    }
}
