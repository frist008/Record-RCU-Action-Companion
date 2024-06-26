package ua.frist008.action.record.ui.navigation

import androidx.activity.ComponentActivity
import androidx.navigation.NavHostController
import timber.log.Timber

class Router(private val navController: NavHostController) {

    fun handleCommand(command: NavCommand) {
        when (command) {
            is NavCommand.BackCommand -> {
                val popped =
                    if (command.screen == null) {
                        navController.popBackStack()
                    } else {
                        navController.popBackStack(
                            route = command,
                            inclusive = true,
                        )
                    }

                if (!popped) {
                    // For enableOnBackInvokedCallback=true from Manifest
                    val activity = navController.context as? ComponentActivity
                    activity?.onBackPressedDispatcher?.onBackPressed()
                        ?: Timber.e("Can't handle command $command")
                }
            }

            else -> navController.navigate(command) {
                val route = navController.currentBackStackEntry?.destination?.route
                popUpTo(route ?: return@navigate) {
                    inclusive = command.replaceScreen
                }
            }
        }
    }
}
