package ua.frist008.action.record.features.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import ua.frist008.action.record.R
import ua.frist008.action.record.core.ui.component.root.DefaultScaffold
import ua.frist008.action.record.core.ui.component.unsupportedUI
import ua.frist008.action.record.core.ui.theme.color.PreviewPalette

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    DefaultScaffold(title = stringResource(R.string.settings_title)) { innerPadding ->
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
