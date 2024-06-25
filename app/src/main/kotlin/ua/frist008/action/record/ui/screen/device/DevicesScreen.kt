package ua.frist008.action.record.ui.screen.device

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import ua.frist008.action.record.R
import ua.frist008.action.record.presentation.impl.DevicesViewModel
import ua.frist008.action.record.ui.component.DefaultScaffold
import ua.frist008.action.record.ui.entity.base.UIState
import ua.frist008.action.record.ui.entity.device.DeviceLoadingState
import ua.frist008.action.record.ui.entity.device.DevicesSuccessState
import ua.frist008.action.record.util.exception.unsupportedUI

// TODO maybe move to only DevicesSuccessScreen
@Composable
fun DevicesScreen() {
    DefaultScaffold(titleRes = R.string.devices_title) { innerPadding ->
        DevicesScreen(innerPadding = innerPadding)
    }
}

@Composable
private fun DevicesScreen(
    viewModel: DevicesViewModel = hiltViewModel(),
    innerPadding: PaddingValues,
) {
    val state by viewModel.state.collectAsState()

    when (val currentState = state) {
        is DevicesSuccessState -> DevicesSuccessScreen(
            state = currentState,
            innerPadding = innerPadding,
            onItemClick = viewModel::onItemClicked,
        )

        is UIState.Error -> DevicesErrorScreen(
            cause = currentState,
            innerPadding = innerPadding,
            onSurfaceClick = viewModel::onRefreshClicked,
        )

        is DeviceLoadingState -> DevicesLoadingScreen(
            state = currentState,
            innerPadding = innerPadding,
            onSurfaceClick = viewModel::onRefreshClicked,
        )

        is UIState.Progress -> {
            DevicesLoadingScreen(
                state = DeviceLoadingState(),
                innerPadding = innerPadding,
            )
        }

        else -> unsupportedUI()
    }

    DisposableEffect(viewModel) {
        viewModel.onInit()
        onDispose { viewModel.onDispose() }
    }
}
