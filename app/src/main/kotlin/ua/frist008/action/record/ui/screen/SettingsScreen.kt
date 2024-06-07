package ua.frist008.action.record.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import ua.frist008.action.record.presentation.impl.SettingsViewModel
import ua.frist008.action.record.ui.entity.SettingsSuccessState
import ua.frist008.action.record.ui.entity.base.UIState
import ua.frist008.action.record.util.exception.unsupportedUI
import ua.frist008.action.record.util.ui.preview.ErrorPreviewProvider

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    when (val currentState = state) {
        is SettingsSuccessState -> SettingsSuccessScreen(currentState)
        is UIState.Error -> SettingsErrorScreen(currentState)

        is UIState.Progress -> {
            // None
        }

        else -> unsupportedUI()
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
private fun SettingsSuccessScreen(@PreviewParameter(SettingsProvider::class) entry: SettingsSuccessState) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Text(
            text = "Android",
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
private fun SettingsErrorScreen(@PreviewParameter(ErrorPreviewProvider::class) cause: Exception) {
}

private class SettingsProvider : PreviewParameterProvider<SettingsSuccessState> {

    override val values = sequenceOf(SettingsSuccessState())
}
