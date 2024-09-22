package ua.frist008.action.record.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

@Suppress("CompositionLocalNaming", "ObjectPropertyName")
private val _LocalNavigator = staticCompositionLocalOf<Router> {
    error("No Navigator provided")
}

val LocalNavigator: Router
    @Composable
    @ReadOnlyComposable
    get() = _LocalNavigator.current

@Composable
fun ProvideNavigators(router: Router, content: @Composable () -> Unit) {
    CompositionLocalProvider(
        _LocalNavigator provides router,
        content = content,
    )
}
