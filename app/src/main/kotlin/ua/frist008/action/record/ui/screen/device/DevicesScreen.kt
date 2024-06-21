package ua.frist008.action.record.ui.screen.device

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import ua.frist008.action.record.R
import ua.frist008.action.record.presentation.impl.DevicesViewModel
import ua.frist008.action.record.ui.entity.base.UIState
import ua.frist008.action.record.ui.entity.device.DeviceLoadingState
import ua.frist008.action.record.ui.entity.device.DevicesSuccessState
import ua.frist008.action.record.ui.theme.color.BrushPalette
import ua.frist008.action.record.ui.theme.color.Palette
import ua.frist008.action.record.util.exception.unsupportedUI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevicesScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.devices_title)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Palette.PURPLE_DARK),
            )
        },
        modifier = Modifier.background(BrushPalette.PURPLE),
    ) { innerPadding ->
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
