package ua.frist008.action.record.features.record

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ua.frist008.action.record.core.ui.UIState
import ua.frist008.action.record.core.ui.component.unsupportedUI
import ua.frist008.action.record.features.record.entity.RecordSuccessState

@Composable
fun RecordScreen(
    pcId: Long,
    viewModel: RecordViewModel = hiltViewModel<RecordViewModel, RecordViewModel.Factory> {
        it.create(pcId)
    },
) {
    val view = LocalView.current
    val state by viewModel.state.collectAsState()

    when (val currentState = state) {
        is RecordSuccessState -> {
            if (currentState.gameActive) {
                RecordSuccessScreen(
                    state = currentState,
                    adListener = viewModel.adListener,
                    onStartClick = viewModel::onStartClick,
                    onResumeClick = viewModel::onResumeClick,
                    onPauseClick = viewModel::onPauseClick,
                    onStopClick = viewModel::onStopClick,
                )
            } else {
                RecordInactiveGameScreen(currentState.storage, currentState.live.isLive)
            }
        }

        is UIState.Progress -> RecordProgressScreen()
        else -> unsupportedUI()
    }

    SideEffect {
        view.keepScreenOn = true
    }

    DisposableEffect(viewModel) {
        view.keepScreenOn = true
        onDispose { view.keepScreenOn = false }
    }
}
