package ua.frist008.action.record.ui.screen.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import ua.frist008.action.record.R
import ua.frist008.action.record.presentation.impl.SettingsViewModel
import ua.frist008.action.record.ui.component.DefaultScaffold
import ua.frist008.action.record.ui.entity.SettingsSuccessState
import ua.frist008.action.record.ui.theme.color.PreviewPalette
import ua.frist008.action.record.util.exception.unsupportedUI

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    DefaultScaffold(titleRes = R.string.settings_title) { innerPadding ->
        val state by viewModel.state.collectAsState()

        when (val currentState = state) {
            is SettingsSuccessState -> SettingsSuccessScreen(currentState, innerPadding)
            else -> unsupportedUI()
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    backgroundColor = PreviewPalette.PURPLE_LIGHT_LONG,
)
@Composable
private fun SettingsSuccessScreen(
    @PreviewParameter(SettingsProvider::class) entry: SettingsSuccessState,
    innerPadding: PaddingValues = PaddingValues(),
) {
    Text(
        text = "Android",
        modifier = Modifier.padding(innerPadding),
    )
}

private class SettingsProvider : PreviewParameterProvider<SettingsSuccessState> {

    override val values = sequenceOf(SettingsSuccessState())
}
