package ua.frist008.action.record.ui.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import ua.frist008.action.record.ui.theme.AppTheme

@Composable
fun DefaultScaffold(
    @StringRes titleRes: Int,
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    DefaultScaffold(
        title = stringResource(titleRes),
        actions = actions,
        content = content,
    )
}

@Composable
fun DefaultScaffold(
    title: String,
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    val containerColor = if (LocalView.current.isInEditMode) {
        Color.Transparent
    } else {
        MaterialTheme.colorScheme.background
    }

    Scaffold(
        topBar = { DefaultTopAppBar(title, actions) },
        content = content,
        containerColor = containerColor,
        contentColor = if (LocalView.current.isInEditMode) {
            AppTheme.colors.onBackground
        } else {
            contentColorFor(containerColor)
        },
    )
}
