package ua.frist008.action.record.features.connection

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleStartEffect
import ua.frist008.action.record.R
import ua.frist008.action.record.features.connection.entity.ConnectionProgressState

@Composable
fun ConnectionScreen(viewModel: ConnectionViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val link = stringResource(R.string.device_error_more_link)

    ConnectionProgressScreen(
        state = state as? ConnectionProgressState ?: ConnectionProgressState(),
        onLinkCLick = { viewModel.onLinkCLick(link) },
    )

    LifecycleStartEffect(viewModel) {
        viewModel.onInit()
        onStopOrDispose { viewModel.onDispose() }
    }
}
