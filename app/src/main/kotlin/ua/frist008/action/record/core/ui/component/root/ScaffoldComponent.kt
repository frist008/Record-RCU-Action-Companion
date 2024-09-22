package ua.frist008.action.record.core.ui.component.root

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import ua.frist008.action.record.core.ui.navigation.Router
import ua.frist008.action.record.core.ui.theme.AppTheme
import ua.frist008.action.record.features.NavCommand

@Composable
fun DefaultScaffold(
    title: String,
    modifier: Modifier = Modifier,
    topAppBarColors: TopAppBarColors = AppTheme.colors.topAppBarColors,
    containerColor: Color = topAppBarColors.containerColor,
    onBackClick: (navigator: Router) -> Unit = { navigator -> navigator(NavCommand.BackCommand()) },
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            DefaultTopAppBar(
                title = title,
                colors = topAppBarColors,
                onBackClick = onBackClick,
                actions = actions,
            )
        },
        content = content,
        containerColor = if (LocalView.current.isInEditMode) Color.Transparent else containerColor,
        contentColor = if (LocalView.current.isInEditMode) {
            AppTheme.colors.onBackground
        } else {
            contentColorFor(containerColor)
        },
    )
}
