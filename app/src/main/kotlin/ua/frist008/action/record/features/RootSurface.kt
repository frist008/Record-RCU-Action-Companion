package ua.frist008.action.record.features

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ua.frist008.action.record.core.presentation.RootNavigationViewModel
import ua.frist008.action.record.core.ui.navigation.ProvideNavigators
import ua.frist008.action.record.core.ui.navigation.Router
import ua.frist008.action.record.core.ui.theme.RootTheme
import ua.frist008.action.record.features.connection.ConnectionScreen
import ua.frist008.action.record.features.record.RecordScreen
import ua.frist008.action.record.features.settings.AboutScreen
import ua.frist008.action.record.features.settings.SettingsScreen

@Composable
fun RootSurface(navigatorViewModel: RootNavigationViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val backStack = rememberNavBackStack(NavCommand.ConnectionScreen)
    val router = remember(context) { Router(backStack, context) }

    RootTheme {
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
            NavDisplay(
                backStack = backStack,
                onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator(),
                ),
                entryProvider = { key ->
                    NavEntry(key) {
                        when (key) {
                            is NavCommand.ConnectionScreen -> ConnectionScreen()
                            is NavCommand.RecordScreen -> RecordScreen()
                            is NavCommand.SettingsScreen -> SettingsScreen()
                            is NavCommand.AboutScreen -> AboutScreen()
                            else -> Unit
                        }
                    }
                },
            )
        }
    }
}