package ua.frist008.action.record.features.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import ua.frist008.action.record.R
import ua.frist008.action.record.core.ui.UIState
import ua.frist008.action.record.core.ui.component.root.DefaultScaffold
import ua.frist008.action.record.core.ui.component.unsupportedUI
import ua.frist008.action.record.features.settings.entity.SettingsSuccessState

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    // The screen gets the events of the settings alone, never the ViewModel
    val actions: SettingsActions = viewModel

    DefaultScaffold(title = stringResource(R.string.settings_title)) { innerPadding ->
        when (val currentState = state) {
            is SettingsSuccessState -> SettingsSuccessScreen(
                state = currentState,
                actions = actions,
                innerPadding = innerPadding,
            )

            is UIState.Progress -> Unit
            else -> unsupportedUI()
        }
    }

    LifecycleResumeEffect(viewModel) {
        viewModel.onResume()
        onPauseOrDispose {}
    }
}
