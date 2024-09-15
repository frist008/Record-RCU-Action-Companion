package ua.frist008.action.record.ui.navigation

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.navigation.NavHostController
import timber.log.Timber

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
                val intent = CustomTabsIntent
                    .Builder()
                    .setShowTitle(true)
                    .build()

                intent.launchUrl(navController.context, Uri.parse(command.url))
            }

            is NavCommand.NewRoot -> navController.navigate(command.newRootScreen) {
                popUpTo(navController.graph.id) {
                    inclusive = command.isReplaceScreen
                }
            }

            else -> navController.navigate(command) {
                val route = navController.currentBackStackEntry?.destination?.route
                popUpTo(route ?: return@navigate) {
                    inclusive = command.isReplaceScreen
                }
            }
        }
    }
}
