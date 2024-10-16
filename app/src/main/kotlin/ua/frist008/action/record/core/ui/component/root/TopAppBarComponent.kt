package ua.frist008.action.record.core.ui.component.root

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.navigation.compose.rememberNavController
import ua.frist008.action.record.BuildConfig
import ua.frist008.action.record.R
import ua.frist008.action.record.core.ui.navigation.LocalNavigator
import ua.frist008.action.record.core.ui.navigation.Router
import ua.frist008.action.record.core.ui.theme.RootThemeScaffoldPreview
import ua.frist008.action.record.features.NavCommand

@Composable
fun DefaultTopAppBar(
    title: String,
    colors: TopAppBarColors,
    backIcon: ImageVector? = Icons.AutoMirrored.Filled.ArrowBack,
    onBackClick: (navigator: Router) -> Unit = { navigator -> navigator(NavCommand.BackCommand()) },
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        title = { Text(title) },
        actions = actions,
        colors = colors,
        navigationIcon = @Composable {
            if (backIcon != null) {
                BackArrowIcon(backIcon = backIcon, onBackClick = onBackClick)
            }
        },
    )
}

@Composable
private inline fun BackArrowIcon(
    backIcon: ImageVector,
    crossinline onBackClick: (navigator: Router) -> Unit,
) {
    val navigator =
        if (BuildConfig.DEBUG && LocalView.current.isEnabled) {
            Router(rememberNavController())
        } else {
            LocalNavigator
        }

    IconButton(onClick = { onBackClick(navigator) }) {
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
