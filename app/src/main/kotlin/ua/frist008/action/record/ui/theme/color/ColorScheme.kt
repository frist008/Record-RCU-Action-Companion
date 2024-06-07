package ua.frist008.action.record.ui.theme.color

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ua.frist008.action.record.R
import ua.frist008.action.record.ui.theme.RootTheme

val DarkColorScheme: ColorScheme = darkColorScheme(
    primary = Palette.PURPLE_DARK,
    onPrimary = Palette.WHITE_LIGHT,
    primaryContainer = Palette.PURPLE_DARK,
    onPrimaryContainer = Palette.WHITE_LIGHT,
    inversePrimary = Palette.PURPLE_LIGHT,
    secondary = Palette.PURPLE_LIGHT,
    onSecondary = Palette.WHITE_LIGHT,
    secondaryContainer = Palette.PURPLE_LIGHT,
    onSecondaryContainer = Palette.WHITE_LIGHT,
    tertiary = Palette.PURPLE_LIGHT,
    onTertiary = Palette.WHITE_LIGHT,
    tertiaryContainer = Palette.PURPLE_LIGHT,
    onTertiaryContainer = Palette.WHITE_LIGHT,
    background = Palette.TRANSPARENT,
    onBackground = Palette.WHITE_LIGHT,
    surface = Palette.PURPLE_DARK,
    onSurface = Palette.WHITE_LIGHT,
    surfaceVariant = Palette.PURPLE_LIGHT,
    onSurfaceVariant = Palette.WHITE_LIGHT,
    surfaceTint = Palette.PURPLE_DARK,
    inverseSurface = Palette.PURPLE_LIGHT,
    inverseOnSurface = Palette.WHITE_LIGHT,
    error = Palette.RED_LIGHT,
    onError = Palette.WHITE_LIGHT,
    errorContainer = Palette.RED_LIGHT,
    onErrorContainer = Palette.WHITE_LIGHT,
    outline = Palette.TRANSPARENT,
    outlineVariant = Palette.TRANSPARENT,
    scrim = Palette.PURPLE_DARK,
    surfaceBright = Palette.PURPLE_LIGHT,
    surfaceContainerHighest = Palette.PURPLE_LIGHT,
    surfaceContainerHigh = Palette.PURPLE_LIGHT,
    surfaceContainer = Palette.PURPLE_DARK,
    surfaceContainerLow = Palette.PURPLE_DARK,
    surfaceContainerLowest = Palette.PURPLE_DARK,
    surfaceDim = Palette.PURPLE_DARK,
)

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
                    title = { Text(stringResource(id = R.string.devices_title)) },
                )
            },
            modifier = Modifier.fillMaxSize(),
        ) {
            Text(text = "Hello World", modifier = Modifier.padding(it))
        }
    }
}
