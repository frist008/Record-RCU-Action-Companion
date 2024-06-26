package ua.frist008.action.record.ui.screen.record

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import ua.frist008.action.record.presentation.impl.RecordViewModel
import ua.frist008.action.record.ui.entity.RecordSuccessState
import ua.frist008.action.record.ui.entity.base.UIState
import ua.frist008.action.record.util.exception.unsupportedUI

@Composable
fun RecordScreen(
    viewModel: RecordViewModel = hiltViewModel(),
    pcId: Long,
) {
    val state by viewModel.state.collectAsState()

    when (val currentState = state) {
        is RecordSuccessState -> RecordSuccessScreen(currentState)

        is UIState.Progress -> {
            viewModel.onInit(pcId)
            RecordProgressScreen()
        }

        else -> unsupportedUI()
    }
}
