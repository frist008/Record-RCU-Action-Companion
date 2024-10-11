package ua.frist008.action.record.core.ui.navigation

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.navigation.NavHostController
import timber.log.Timber
import ua.frist008.action.record.features.NavCommand

class Router(private val navController: NavHostController) : (NavCommand) -> Unit {

    override operator fun invoke(command: NavCommand) {
        when (command) {
            is NavCommand.BackCommand -> {
                val isPopped =
                    if (command.backToScreen == null) {
                        navController.popBackStack()
                    } else {
                        navController.popBackStack(
                            route = command,
                            inclusive = true,
                        )
                    }

                if (!isPopped) {
                    // For enableOnBackInvokedCallback=true from Manifest
                    val activity = navController.context as? ComponentActivity
                    activity?.onBackPressedDispatcher?.onBackPressed()
                        ?: Timber.e("Can't handle command $command")
                }
            }

            is NavCommand.Link -> {
                val customTabsIntent = CustomTabsIntent
                    .Builder()
                    .setShowTitle(true)
                    .build()

                customTabsIntent.launchUrl(navController.context, Uri.parse(command.url))
            }

            else -> navController.navigate(command) {
                val route = navController.currentBackStackEntry?.destination?.route
                popUpTo(route ?: return@navigate) {
                    inclusive = command.isReplaceScreen
                    saveState = true
                }
                restoreState = true
            }
        }
    }
}
