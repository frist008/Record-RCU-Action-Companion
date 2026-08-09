package ua.frist008.action.record.core.ui.navigation

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.ShareCompat
import androidx.core.net.toUri
import androidx.navigation3.runtime.NavKey
import timber.log.Timber
import ua.frist008.action.record.core.util.media.MimeType
import ua.frist008.action.record.features.NavCommand
import ua.frist008.action.record.features.NewRoot

class Router(
    private val backStack: MutableList<NavKey>,
    private val context: Context,
) : (NavCommand) -> Unit {

    override operator fun invoke(command: NavCommand) {
        when (command) {
            is NavCommand.BackCommand -> back(command)
            is NewRoot -> {
                backStack.clear()
                backStack.add(command.newRootScreen)
            }

            is NavCommand.Link -> openLink(command.url)
            is NavCommand.App -> openApp(command.appPackage, command.url)
            is NavCommand.Share -> share(command)
            else -> {
                if (command.isReplaceScreen) backStack.removeLastOrNull()
                backStack.add(command)
            }
        }
    }

    private fun back(command: NavCommand.BackCommand) {
        if (command.backToScreen == null) {
            backStack.removeLastOrNull()
        } else {
            val targetClass = command.backToScreen::class
            val idx = backStack.indexOfLast { it::class == targetClass }
            if (idx >= 0) {
                backStack.subList(idx + 1, backStack.size).clear()
            } else {
                Timber.d("BackCommand: ${targetClass.simpleName} not found in back stack")
            }
        }
    }

    private fun openLink(url: String) {
        CustomTabsIntent.Builder()
            .setShareState(CustomTabsIntent.SHARE_STATE_ON)
            .setShowTitle(true)
            .build()
            .launchUrl(context, url.toUri())
    }

    private fun openApp(appPackage: String, url: String) {
        CustomTabsIntent.Builder()
            .setShareState(CustomTabsIntent.SHARE_STATE_ON)
            .build()
            .also { it.intent.setPackage(appPackage) }
            .launchUrl(context, url.toUri())
    }

    private fun share(command: NavCommand.Share) {
        val subject = context.getString(command.messageRes)
        ShareCompat.IntentBuilder(context)
            .setType(MimeType.PLAIN_TEXT)
            .setSubject(subject)
            .setText(command.url)
            .intent
            .also { context.startActivity(it) }
    }
}
