package ua.frist008.action.record.ui.surface

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ua.frist008.action.record.presentation.impl.RootNavigationViewModel
import ua.frist008.action.record.ui.navigation.NavCommand
import ua.frist008.action.record.ui.navigation.Router
import ua.frist008.action.record.ui.screen.DevicesScreen
import ua.frist008.action.record.ui.screen.RecordScreen
import ua.frist008.action.record.ui.screen.SettingsScreen
import ua.frist008.action.record.ui.theme.RootTheme

@Composable
fun RootSurface(navigatorViewModel: RootNavigationViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val router = remember { Router(navController) }

    RootTheme {
        LaunchedEffect(NavCommand.ROOT_LAYER) {
            navigatorViewModel
                .navigator
                .onEach(router::handleCommand)
                .launchIn(this)
        }

        NavHost(navController, startDestination = NavCommand.DevicesScreen.route) {
            composable(NavCommand.DevicesScreen.route) {
                DevicesScreen()
            }
            composable(NavCommand.RecordScreen.route) {
                RecordScreen()
            }
            composable(NavCommand.SettingsScreen.route) {
                SettingsScreen()
            }
        }
    }
}
