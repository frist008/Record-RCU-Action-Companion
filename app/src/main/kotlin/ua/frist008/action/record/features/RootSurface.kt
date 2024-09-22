package ua.frist008.action.record.features

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ua.frist008.action.record.core.presentation.RootNavigationViewModel
import ua.frist008.action.record.core.ui.navigation.ProvideNavigators
import ua.frist008.action.record.core.ui.navigation.Router
import ua.frist008.action.record.core.ui.theme.RootTheme
import ua.frist008.action.record.features.device.DevicesScreen
import ua.frist008.action.record.features.record.RecordScreen
import ua.frist008.action.record.features.settings.SettingsScreen

@Composable
fun RootSurface(navigatorViewModel: RootNavigationViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val router = remember { Router(navController) }

    RootTheme {
        val context = LocalContext.current

        LaunchedEffect(navigatorViewModel) {
            navigatorViewModel.navigator
                .onEach(router)
                .launchIn(this)

            var lastToast: Toast? = null

            navigatorViewModel.toastFlow
                .onEach {
                    lastToast?.cancel()
                    lastToast = Toast.makeText(context, it, Toast.LENGTH_SHORT)
                    lastToast?.show()
                }
                .launchIn(this)
        }

        ProvideNavigators(router) {
            NavHost(
                navController = navController,
                startDestination = NavCommand.DevicesScreen::class,
            ) {
                composable<NavCommand.DevicesScreen> {
                    DevicesScreen()
                }
                composable<NavCommand.RecordScreen> {
                    RecordScreen()
                }
                composable<NavCommand.SettingsScreen> {
                    SettingsScreen()
                }
            }
        }
    }
}
