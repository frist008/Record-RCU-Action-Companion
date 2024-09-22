package ua.frist008.action.record.core.ui.theme

import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ua.frist008.action.record.R
import ua.frist008.action.record.core.ui.component.root.DefaultScaffold
import ua.frist008.action.record.core.ui.theme.color.AppColorScheme
import ua.frist008.action.record.core.ui.theme.typography.AppTypography

@Composable
fun RootTheme(content: @Composable () -> Unit) {
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val activity = view.context as ComponentActivity

            activity.enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.dark(AppTheme.colors.statusBar.toArgb()),
                navigationBarStyle = SystemBarStyle.dark(AppTheme.colors.navigationBar.toArgb()),
            )

            activity.window.setBackgroundDrawableResource(R.drawable.window_background)
        }
    }

    MaterialTheme(
        colorScheme = AppTheme.colors(),
        shapes = AppTheme.shapes,
        typography = AppTheme.typography(),
        content = content,
    )
}

internal object AppTheme {
    val colors = AppColorScheme
    val typography = AppTypography
    val shapes = Shapes()
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RootThemePreview(content: @Composable () -> Unit = { Text("Hello World") }) {
    RootTheme { content() }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RootThemeScaffoldPreview(
    @StringRes titleRes: Int = R.string.app_name,
    content: @Composable (innerPadding: PaddingValues) -> Unit = {
        Text(text = "Hello World", modifier = Modifier.padding(it))
    },
) {
    RootThemePreview {
        DefaultScaffold(stringResource(id = titleRes)) {
            content(it)
        }
    }
}
