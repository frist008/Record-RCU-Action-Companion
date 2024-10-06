package ua.frist008.action.record.features.device

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleStartEffect
import ua.frist008.action.record.R
import ua.frist008.action.record.core.ui.UIState
import ua.frist008.action.record.features.device.entity.DevicesProgressState
import ua.frist008.action.record.features.device.entity.DevicesSuccessState

@Composable
fun DevicesScreen(viewModel: DevicesViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val link = stringResource(R.string.device_error_more_link)

    when (val currentState = state) {
        is DevicesSuccessState -> DevicesSuccessScreen(
            state = currentState,
            onItemClick = viewModel::onItemClicked,
        )

        is DevicesProgressState -> DevicesProgressScreen(
            state = currentState,
            onLinkCLick = { viewModel.onLinkCLick(link) },
        )

        is UIState.Error -> DevicesErrorScreen(currentState)
        else -> DevicesProgressScreen(DevicesProgressState())
    }

    LifecycleStartEffect(viewModel) {
        viewModel.onInit()
        onStopOrDispose { viewModel.onDispose() }
    }
}
