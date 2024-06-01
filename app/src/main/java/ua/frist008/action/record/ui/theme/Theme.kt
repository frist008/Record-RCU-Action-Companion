package ua.frist008.action.record.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import ua.frist008.action.record.ui.theme.color.DarkColorScheme
import ua.frist008.action.record.ui.theme.color.LightColorScheme
import ua.frist008.action.record.ui.theme.typography.Typography

@Composable
fun RootTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = getColorScheme(isDarkTheme),
        typography = Typography,
        content = content,
    )
}

@Composable @ReadOnlyComposable
private fun getColorScheme(isDarkTheme: Boolean) =
    if (isDarkTheme) DarkColorScheme else LightColorScheme
