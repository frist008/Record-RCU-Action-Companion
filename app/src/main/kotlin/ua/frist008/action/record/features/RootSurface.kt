package ua.frist008.action.record.features

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
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
    val context = LocalContext.current
    val activity = context as ComponentActivity
    val backStack = rememberNavBackStack(NavCommand.DevicesScreen)
    val router = remember(context) { Router(backStack, context) }

    val vmStoreOwners = remember { HashMap<Any, NavScopedViewModelStoreOwner>() }
    val backStackSnapshot = backStack.toList()
    LaunchedEffect(backStackSnapshot) {
        val currentKeys = backStackSnapshot.toHashSet()
        vmStoreOwners.keys.filter { it !in currentKeys }.forEach { key ->
            vmStoreOwners.remove(key)?.viewModelStore?.clear()
        }
    }

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
                entryProvider = { key ->
                    NavEntry(key) {
                        val owner = remember(key) {
                            vmStoreOwners.getOrPut(key) { NavScopedViewModelStoreOwner(activity) }
                        }
                        CompositionLocalProvider(LocalViewModelStoreOwner provides owner) {
                            when (key) {
                                is NavCommand.DevicesScreen -> DevicesScreen()
                                is NavCommand.RecordScreen -> RecordScreen(pcId = key.pcId)
                                is NavCommand.SettingsScreen -> SettingsScreen()
                                else -> Unit
                            }
                        }
                    }
                },
            )
        }
    }
}

private class NavScopedViewModelStoreOwner(
    private val activity: ComponentActivity,
) : ViewModelStoreOwner, HasDefaultViewModelProviderFactory {

    override val viewModelStore = ViewModelStore()

    override val defaultViewModelProviderFactory: ViewModelProvider.Factory
        get() = activity.defaultViewModelProviderFactory

    override val defaultViewModelCreationExtras
        get() = activity.defaultViewModelCreationExtras
}
