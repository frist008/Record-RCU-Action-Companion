package ua.frist008.action.record.ui.screen.record

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalView
import androidx.hilt.navigation.compose.hiltViewModel
import ua.frist008.action.record.presentation.impl.RecordViewModel
import ua.frist008.action.record.ui.entity.base.UIState
import ua.frist008.action.record.ui.entity.record.RecordSuccessState
import ua.frist008.action.record.util.exception.unsupportedUI

@Composable
fun RecordScreen(viewModel: RecordViewModel = hiltViewModel()) {
    val view = LocalView.current
    val state by viewModel.state.collectAsState()

    when (val currentState = state) {
        is RecordSuccessState -> {
            if (currentState.gameActive) {
                RecordSuccessScreen(
                    state = currentState,
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

    DisposableEffect(viewModel) {
        view.keepScreenOn = true
        onDispose { view.keepScreenOn = false }
    }
}
