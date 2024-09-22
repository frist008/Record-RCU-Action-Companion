package ua.frist008.action.record.features.record

import android.os.Vibrator
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import timber.log.Timber
import ua.frist008.action.record.R
import ua.frist008.action.record.analytics.Analytics
import ua.frist008.action.record.core.presentation.BaseViewModel
import ua.frist008.action.record.core.presentation.dependency.PresentationDependenciesDelegate
import ua.frist008.action.record.core.presentation.dependency.StateOwner.Companion.state
import ua.frist008.action.record.core.ui.UIState
import ua.frist008.action.record.core.util.sound.VibratorEffect
import ua.frist008.action.record.core.util.sound.vibrate
import ua.frist008.action.record.data.network.DisconnectException
import ua.frist008.action.record.features.NavCommand
import ua.frist008.action.record.features.record.entity.FixedRecordCommand
import ua.frist008.action.record.features.record.entity.RecordSuccessState
import ua.frist008.action.record.features.record.entity.StreamingRecordCommand
import java.net.BindException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel class RecordViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dependencies: PresentationDependenciesDelegate,
    private val recordRepository: RecordRepository,
    private val vibrator: Vibrator,
) : BaseViewModel(dependencies) {

    init {
        val pcId = savedStateHandle.toRoute<NavCommand.RecordScreen>().pcId

        launch {
            while (viewModelScope.isActive) {
                try {
                    recordRepository.connect(pcId)
                } catch (e: SocketTimeoutException) {
                    mutableState.emit(UIState.Progress())
                    Timber.d(e)
                }
            }
        }

        launch {
            recordRepository.recordFlow.collectLatest {
                if (it.connected) {
                    val state = it.toUI(state.value as? RecordSuccessState?)
                    mutableState.emit(state)
                    Analytics.log(state)
                } else {
                    mutableState.emit(UIState.Progress())
                }
            }
        }

        launch {
            Analytics.subscribe()
        }
    }

    fun onStartClick(state: RecordSuccessState = state()) {
        vibrator.vibrate(VibratorEffect.PRIMITIVE_QUICK_RISE)

        launch {
            if (state.live.isLive) {
                recordRepository.sendCommand(FixedRecordCommand.START)
            } else {
                recordRepository.sendCommand(StreamingRecordCommand.START)
            }
        }
    }

    fun onPauseClick() {
        vibrator.vibrate(VibratorEffect.PRIMITIVE_SLOW_RISE)

        launch {
            recordRepository.sendCommand(FixedRecordCommand.PAUSE)
        }
    }

    fun onResumeClick() {
        vibrator.vibrate(VibratorEffect.PRIMITIVE_QUICK_RISE)

        launch {
            recordRepository.sendCommand(FixedRecordCommand.RESUME)
        }
    }

    fun onStopClick(state: RecordSuccessState = state()) {
        vibrator.vibrate(VibratorEffect.PRIMITIVE_CLICK)

        launch {
            if (state.live.isLive) {
                recordRepository.sendCommand(FixedRecordCommand.STOP)
            } else {
                recordRepository.sendCommand(StreamingRecordCommand.STOP)
            }
        }
    }

    override suspend fun onFailure(cause: Throwable) {
        when (cause) {
            is DisconnectException -> navigator.emit(NavCommand.BackCommand())

            is BindException -> {
                // TODO add timeout on device screen for reconnect
                toastFlow.emit(R.string.record_error_already_used)
                navigator.emit(NavCommand.BackCommand())
            }

            else -> {
                toastFlow.emit(R.string.error_unknown_error)
                navigator.emit(NavCommand.BackCommand())
                super.onFailure(cause)
            }
        }
    }
}
