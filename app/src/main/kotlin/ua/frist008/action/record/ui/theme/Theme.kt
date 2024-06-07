package ua.frist008.action.record.ui.theme

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ua.frist008.action.record.R
import ua.frist008.action.record.ui.theme.color.DarkColorScheme
import ua.frist008.action.record.ui.theme.typography.Typography

@Composable
fun RootTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = getColorScheme(),
        typography = Typography,
        content = content,
    )
}

@Composable @ReadOnlyComposable
private fun getColorScheme() = DarkColorScheme

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RootThemePreview() {
    RootTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = stringResource(id = R.string.devices_title))
                    },
                )
            },
            modifier = Modifier.fillMaxSize(),
        ) {
            Text(text = "Hello World", modifier = Modifier.padding(it))
        }
    }
}
