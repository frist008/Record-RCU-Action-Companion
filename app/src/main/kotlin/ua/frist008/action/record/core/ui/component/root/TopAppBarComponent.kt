package ua.frist008.action.record.core.ui.component.root

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ua.frist008.action.record.R
import ua.frist008.action.record.core.ui.navigation.LocalNavigator
import ua.frist008.action.record.core.ui.navigation.Router
import ua.frist008.action.record.core.ui.resource.Icons
import ua.frist008.action.record.core.ui.resource.svg.ArrowBack
import ua.frist008.action.record.core.ui.theme.RootThemeScaffoldPreview
import ua.frist008.action.record.features.NavCommand

@Composable
fun DefaultTopAppBar(
    title: String,
    colors: TopAppBarColors,
    backIcon: ImageVector? = Icons.ArrowBack,
    onBackClick: (navigator: Router) -> Unit = { navigator -> navigator(NavCommand.BackCommand()) },
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        title = { Text(title) },
        actions = actions,
        colors = colors,
        navigationIcon = {
            if (backIcon != null) {
                BackArrowIcon(backIcon = backIcon, onBackClick = onBackClick)
            }
        },
    )
}

@Composable
private fun BackArrowIcon(
    backIcon: ImageVector,
    onBackClick: (navigator: Router) -> Unit,
) {
    val navigator = if (LocalView.current.isInEditMode) null else LocalNavigator

    IconButton(onClick = { navigator?.let(onBackClick) }) {
        Icon(
            imageVector = backIcon,
            contentDescription = stringResource(id = R.string.app_name),
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TopAppBarPreview() {
    RootThemeScaffoldPreview(R.string.devices_title) {}
}
