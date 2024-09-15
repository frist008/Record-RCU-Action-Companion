package ua.frist008.action.record.ui.surface

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ua.frist008.action.record.presentation.impl.RootNavigationViewModel
import ua.frist008.action.record.ui.navigation.NavCommand
import ua.frist008.action.record.ui.navigation.Router
import ua.frist008.action.record.ui.screen.device.DevicesScreen
import ua.frist008.action.record.ui.screen.record.RecordScreen
import ua.frist008.action.record.ui.screen.settings.SettingsScreen
import ua.frist008.action.record.ui.theme.RootTheme

@Composable
fun RootSurface(navigatorViewModel: RootNavigationViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val router = remember { Router(navController) }

    RootTheme {
        LaunchedEffect(NavCommand.ROOT_LAYER) {
            navigatorViewModel
                .navigator
                .onEach(router)
                .launchIn(this)
        }

        NavHost(navController = navController, startDestination = NavCommand.DevicesScreen::class) {
            composable<NavCommand.DevicesScreen> {
                DevicesScreen()
            }
            composable<NavCommand.RecordScreen> {
                RecordScreen(pcId = it.toRoute<NavCommand.RecordScreen>().pcId)
            }
            composable<NavCommand.SettingsScreen> {
                SettingsScreen()
            }
        }
    }
}
