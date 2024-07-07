package ua.frist008.action.record.ui.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import ua.frist008.action.record.ui.theme.AppTheme

@Composable
@OptIn(ExperimentalMaterial3Api::class)
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
@OptIn(ExperimentalMaterial3Api::class)
private fun defaultTopAppBarColors(): TopAppBarColors =
    TopAppBarDefaults.topAppBarColors(containerColor = AppTheme.colors.primary)
