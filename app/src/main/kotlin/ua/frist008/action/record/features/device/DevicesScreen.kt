package ua.frist008.action.record.features.device

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import ua.frist008.action.record.core.ui.UIState
import ua.frist008.action.record.core.ui.component.unsupportedUI
import ua.frist008.action.record.features.device.entity.DeviceLoadingState
import ua.frist008.action.record.features.device.entity.DevicesSuccessState

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

        is UIState.Progress -> DevicesLoadingScreen(DeviceLoadingState())
        else -> unsupportedUI()
    }

    LifecycleResumeEffect(viewModel) {
        viewModel.onInit()
        onPauseOrDispose { viewModel.onDispose() }
    }
}
