package ua.frist008.action.record.ui.screen.device

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import ua.frist008.action.record.presentation.impl.DevicesViewModel
import ua.frist008.action.record.ui.entity.base.UIState
import ua.frist008.action.record.ui.entity.device.DeviceLoadingState
import ua.frist008.action.record.ui.entity.device.DevicesSuccessState
import ua.frist008.action.record.util.exception.unsupportedUI

@Composable
fun DevicesScreen(viewModel: DevicesViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    when (val currentState = state) {
        is DevicesSuccessState -> DevicesSuccessScreen(
            state = currentState,
            onItemClick = viewModel::onItemClicked,
        )

        is UIState.Error -> DevicesErrorScreen(
            cause = currentState,
            onSurfaceClick = viewModel::onRefreshClicked,
        )

        is DeviceLoadingState -> DevicesLoadingScreen(
            state = currentState,
            onSurfaceClick = viewModel::onRefreshClicked,
            onLinkCLick = viewModel::onLinkCLick,
        )

        is UIState.Progress -> {
            DevicesLoadingScreen(DeviceLoadingState())
        }

        else -> unsupportedUI()
    }

    LifecycleResumeEffect(viewModel) {
        viewModel.onInit()
        onPauseOrDispose { viewModel.onDispose() }
    }
}
