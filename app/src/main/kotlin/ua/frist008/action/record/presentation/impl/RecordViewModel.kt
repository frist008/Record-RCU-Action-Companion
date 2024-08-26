package ua.frist008.action.record.presentation.impl

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import timber.log.Timber
import ua.frist008.action.record.data.repository.RecordRepository
import ua.frist008.action.record.domain.entity.type.FixedRecordCommand
import ua.frist008.action.record.domain.entity.type.StreamingRecordCommand
import ua.frist008.action.record.presentation.base.BaseViewModel
import ua.frist008.action.record.presentation.base.dependency.PresentationDependenciesDelegate
import ua.frist008.action.record.presentation.base.dependency.StateOwner.Companion.state
import ua.frist008.action.record.ui.entity.base.UIState
import ua.frist008.action.record.ui.entity.record.RecordSuccessState
import ua.frist008.action.record.ui.navigation.NavCommand
import ua.frist008.action.record.util.exception.DisconnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel class RecordViewModel @Inject constructor(
    dependencies: PresentationDependenciesDelegate,
    private val recordRepository: RecordRepository,
) : BaseViewModel(dependencies) {

    fun onInit(pcId: Long) {
        launch {
            while (isActive) {
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

    override fun onCleared() {
        super.onCleared()
        Timber.d("onCleared")
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
