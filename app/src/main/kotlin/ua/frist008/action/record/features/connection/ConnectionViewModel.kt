package ua.frist008.action.record.features.connection

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retryWhen
import timber.log.Timber
import ua.frist008.action.record.core.presentation.BaseViewModel
import ua.frist008.action.record.core.presentation.dependency.PresentationDependenciesDelegate
import ua.frist008.action.record.core.util.concurrent.timer
import ua.frist008.action.record.features.NavCommand
import ua.frist008.action.record.features.NewRoot
import ua.frist008.action.record.features.connection.entity.ConnectionProgressState
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@HiltViewModel class ConnectionViewModel @Inject constructor(
    dependencies: PresentationDependenciesDelegate,
    private val connectionRepository: ConnectionRepository,
) : BaseViewModel(dependencies) {

    private val timerJob = atomic<Job?>(null)
    private val scanJob = atomic<Job?>(null)
    private val disposeJob = atomic<Job?>(null)

    fun onInit() {
        Timber.d("onInit")
        disposeJob.value?.cancel()
        restartTimer()
        restartScan()
    }

    // TODO For version 1.2: move work with Repository to ForegroundService
    private fun restartScan() {
        Timber.i("start Scan")

        launch(scanJob) {
            val device = connectionRepository
                .get()
                .retryWhen { cause, _ ->
                    val isNetworkError = cause is IOException

                    if (isNetworkError) {
                        Timber.w(cause)
                        showProgress()
                        delay(SCAN_RETRY_DELAY)
                    }

                    isNetworkError
                }
                .onEach { if (it == null) showProgress() }
                .firstOrNull { it != null }

            if (device != null) {
                openRecord()
            }
        }
    }

    private suspend fun showProgress() {
        if (mutableState.value !is ConnectionProgressState) {
            Timber.d("Start Progress")
            mutableState.emit(ConnectionProgressState())
        }
    }

    private fun restartTimer(durationDelay: Duration = 0.milliseconds) {
        val emit: suspend (ConnectionProgressState) -> Unit = { state ->
            if (mutableState.value is ConnectionProgressState) {
                Timber.d("Emit Progress - $state")
                mutableState.emit(state)
            }
        }

        launch(timerJob) {
            delay(durationDelay)
            timer(
                durationMs = TIMER_MS,
                delayEventMs = 1.seconds.inWholeMilliseconds,
                onNextEvent = { durationLeft -> emit(ConnectionProgressState(durationLeft)) },
                onFinished = {
                    emit(ConnectionProgressState())
                    Timber.d("Restart Progress")
                    restartTimer(2.seconds)
                },
            )
        }
    }

    private suspend fun openRecord() {
        Timber.i("open RecordScreen")
        navigator.emit(NewRoot(NavCommand.RecordScreen))
    }

    fun onLinkCLick(link: String) {
        launch(Dispatchers.Main.immediate) {
            navigator.emit(NavCommand.Link(link))
        }
    }

    override suspend fun onFailure(cause: Throwable) {
        when (cause) {
            is SocketTimeoutException,
            is SocketException,
                -> Timber.v(cause)

            else -> super.onFailure(cause)
        }
    }

    fun onDispose() {
        Timber.d("onDispose")
        scanJob.value?.cancel()
        timerJob.value?.cancel()

        launch(disposeJob) {
            delay(1.seconds)
            mutableState.emit(ConnectionProgressState())
        }
    }

    companion object {

        private const val TIMER_MS = 5000L

        /** Pause before the search starts again after the network failed it */
        private val SCAN_RETRY_DELAY = 1.seconds
    }
}