package ua.frist008.action.record.ui.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

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
    Scaffold(
        topBar = { DefaultTopAppBar(title, actions) },
        content = content,
    )
}
