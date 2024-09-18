package ua.frist008.action.record.core.ui.component.root

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import ua.frist008.action.record.core.ui.theme.AppTheme

@Composable
fun DefaultTopAppBar(
    title: String,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        title = { Text(title) },
        actions = actions,
        colors = defaultTopAppBarColors(),
    )
}

@Composable
private fun defaultTopAppBarColors(): TopAppBarColors =
    TopAppBarDefaults.topAppBarColors(
        titleContentColor = AppTheme.colors.onPrimary,
        navigationIconContentColor = AppTheme.colors.onPrimary,
        actionIconContentColor = AppTheme.colors.onPrimary,
        containerColor = AppTheme.colors.primary,
    )
