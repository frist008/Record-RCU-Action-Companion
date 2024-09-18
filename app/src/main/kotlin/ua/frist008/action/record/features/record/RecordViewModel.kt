package ua.frist008.action.record.features.record

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import timber.log.Timber
import ua.frist008.action.record.core.presentation.BaseViewModel
import ua.frist008.action.record.core.presentation.dependency.PresentationDependenciesDelegate
import ua.frist008.action.record.core.presentation.dependency.StateOwner.Companion.state
import ua.frist008.action.record.core.ui.UIState
import ua.frist008.action.record.data.network.DisconnectException
import ua.frist008.action.record.features.NavCommand
import ua.frist008.action.record.features.record.entity.FixedRecordCommand
import ua.frist008.action.record.features.record.entity.RecordSuccessState
import ua.frist008.action.record.features.record.entity.StreamingRecordCommand
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel class RecordViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dependencies: PresentationDependenciesDelegate,
    private val recordRepository: RecordRepository,
) : BaseViewModel(dependencies) {

    init {
        val pcId = savedStateHandle.toRoute<NavCommand.RecordScreen>().pcId

        launch {
            while (scope.isActive) {
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
                    mutableState.emit(it.toUI(state.value as? RecordSuccessState?))
                } else {
                    mutableState.emit(UIState.Progress())
                }
            }
        }
    }

    fun onStartClick(state: RecordSuccessState = state()) {
        launch {
            if (state.live.isLive) {
                recordRepository.sendCommand(FixedRecordCommand.START)
            } else {
                recordRepository.sendCommand(StreamingRecordCommand.START)
            }
        }
    }

    fun onPauseClick(state: RecordSuccessState = state()) {
        launch {
            recordRepository.sendCommand(FixedRecordCommand.PAUSE)
        }
    }

    fun onResumeClick(state: RecordSuccessState = state()) {
        launch {
            recordRepository.sendCommand(FixedRecordCommand.RESUME)
        }
    }

    fun onStopClick(state: RecordSuccessState = state()) {
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
            else -> {
                navigator.emit(NavCommand.BackCommand())
                Timber.e(cause)
            }
        }
    }
}
