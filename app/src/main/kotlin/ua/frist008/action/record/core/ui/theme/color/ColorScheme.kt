package ua.frist008.action.record.core.ui.theme.color

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ua.frist008.action.record.R
import ua.frist008.action.record.core.ui.theme.RootTheme

@Immutable data object AppColorScheme : () -> ColorScheme {

    val primary: Color = Palette.PURPLE_DARK
    val onPrimary: Color = Palette.WHITE_LIGHT
    val primaryContainer: Color = Palette.PURPLE_DARK
    val onPrimaryContainer: Color = Palette.WHITE_LIGHT
    val inversePrimary: Color = Palette.PURPLE_LIGHT
    val secondary: Color = Palette.PURPLE_LIGHT
    val onSecondary: Color = Palette.WHITE_LIGHT
    val secondaryContainer: Color = Palette.PURPLE_LIGHT
    val onSecondaryContainer: Color = Palette.WHITE_LIGHT
    val tertiary: Color = Palette.WHITE_DARK
    val onTertiary: Color = Palette.WHITE_LIGHT
    val tertiaryContainer: Color = Palette.PURPLE_LIGHT
    val onTertiaryContainer: Color = Palette.WHITE_LIGHT
    val background: Color = Palette.TRANSPARENT
    val onBackground: Color = Palette.WHITE_LIGHT
    val surface: Color = Palette.PURPLE_DARK
    val onSurface: Color = Palette.WHITE_LIGHT
    val surfaceVariant: Color = Palette.PURPLE_LIGHT
    val onSurfaceVariant: Color = Palette.WHITE_LIGHT
    val surfaceTint: Color = Palette.PURPLE_DARK
    val inverseSurface: Color = Palette.PURPLE_LIGHT
    val inverseOnSurface: Color = Palette.WHITE_LIGHT
    val error: Color = Palette.RED_LIGHT
    val warning = Palette.YELLOW_LIGHT
    val onError: Color = Palette.WHITE_LIGHT
    val errorContainer: Color = Palette.RED_LIGHT
    val onErrorContainer: Color = Palette.WHITE_LIGHT
    val outline: Color = Palette.TRANSPARENT
    val outlineVariant: Color = Palette.TRANSPARENT
    val scrim: Color = Palette.PURPLE_DARK
    val surfaceBright: Color = Palette.PURPLE_LIGHT
    val surfaceDim: Color = Palette.PURPLE_LIGHT
    val surfaceContainer: Color = Palette.PURPLE_LIGHT
    val surfaceContainerHigh: Color = Palette.PURPLE_DARK
    val surfaceContainerHighest: Color = Palette.PURPLE_DARK
    val surfaceContainerLow: Color = Palette.PURPLE_DARK
    val surfaceContainerLowest: Color = Palette.PURPLE_DARK

    val statusBar = primary
    val navigationBar = Palette.TRANSPARENT
    val link = Palette.BLUE_LIGHT
    val online = Palette.GREEN_LIGHT
    val offline = error

    val fps = Palette.WHITE_NEUTRAL

    val hint = Palette.WHITE_NEUTRAL

    val record = Palette.GREEN_LIGHT
    val pause = Palette.YELLOW_LIGHT
    val stop = Palette.RED_LIGHT
    val stopDisabled = Palette.RED_DARK

    val windowBackground = BrushPalette.PURPLE

    private val materialColors: ColorScheme = darkColorScheme(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primaryContainer,
        onPrimaryContainer = onPrimaryContainer,
        inversePrimary = inversePrimary,
        secondary = secondary,
        onSecondary = onSecondary,
        secondaryContainer = secondaryContainer,
        onSecondaryContainer = onSecondaryContainer,
        tertiary = tertiary,
        onTertiary = onTertiary,
        tertiaryContainer = tertiaryContainer,
        onTertiaryContainer = onTertiaryContainer,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        surfaceVariant = surfaceVariant,
        onSurfaceVariant = onSurfaceVariant,
        surfaceTint = surfaceTint,
        inverseSurface = inverseSurface,
        inverseOnSurface = inverseOnSurface,
        error = error,
        onError = onError,
        errorContainer = errorContainer,
        onErrorContainer = onErrorContainer,
        outline = outline,
        outlineVariant = outlineVariant,
        scrim = scrim,
        surfaceBright = surfaceBright,
        surfaceDim = surfaceDim,
        surfaceContainer = surfaceContainer,
        surfaceContainerHigh = surfaceContainerHigh,
        surfaceContainerHighest = surfaceContainerHighest,
        surfaceContainerLow = surfaceContainerLow,
        surfaceContainerLowest = surfaceContainerLowest,
    )

    override operator fun invoke(): ColorScheme = materialColors
}

@Immutable object PreviewPalette {

    const val PURPLE_DARK_LONG: Long = 0xFF031F33
    const val PURPLE_LIGHT_LONG: Long = 0xFF351E58
}

@Immutable private object Palette {

    val TRANSPARENT = Color(0x00000000)

    val WHITE_LIGHT = Color(0xFFFFFFFF)
    val WHITE_NEUTRAL = Color(0xB2FFFFFF)
    val WHITE_DARK = Color(0x70FFFFFF)

    val GREEN_LIGHT = Color(0xFF1ABC9C)
    val GREEN_NEUTRAL = Color(0xB21ABC9C)
    val GREEN_DARK = Color(0xFF36685E)
    val GREEN_TRANSPARENT = Color(0x001ABC9C)

    val YELLOW_LIGHT = Color(0xFFF6BD60)

    val RED_LIGHT = Color(0xFFE74C3C)
    val RED_NEUTRAL = Color(0xB2E74C3C)
    val RED_DARK = Color(0xFF89271D)
    val RED_TRANSPARENT = Color(0x00E74C3C)

    val BLUE_LIGHT = Color(0xFF7AEFFF)

    val PURPLE_DARK = Color(PreviewPalette.PURPLE_DARK_LONG)
    val PURPLE_LIGHT = Color(PreviewPalette.PURPLE_LIGHT_LONG)
}

@Immutable private object BrushPalette {

    val PURPLE = Brush.verticalGradient(
        colors = listOf(Palette.PURPLE_DARK, Palette.PURPLE_LIGHT),
    )
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
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
