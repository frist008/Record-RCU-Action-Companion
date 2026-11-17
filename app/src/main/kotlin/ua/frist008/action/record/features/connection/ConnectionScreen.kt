package ua.frist008.action.record.features.connection

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleStartEffect
import ua.frist008.action.record.R
import ua.frist008.action.record.features.connection.entity.ConnectionPermissionState
import ua.frist008.action.record.features.connection.entity.ConnectionProgressState

@Composable
fun ConnectionScreen(viewModel: ConnectionViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val link = stringResource(R.string.device_error_more_link)
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = viewModel::onPermissionResult,
    )

    when (val currentState = state) {
        is ConnectionPermissionState -> ConnectionPermissionScreen(
            state = currentState,
            onContinueClick = { permissionLauncher.launch(viewModel.localNetworkPermission) },
            onSettingsClick = viewModel::onSettingsClick,
        )

        else -> ConnectionProgressScreen(
            state = currentState as? ConnectionProgressState ?: ConnectionProgressState(),
            onLinkCLick = { viewModel.onLinkCLick(link) },
        )
    }

    LifecycleStartEffect(viewModel) {
        viewModel.onInit()
        onStopOrDispose { viewModel.onDispose() }
    }
}
