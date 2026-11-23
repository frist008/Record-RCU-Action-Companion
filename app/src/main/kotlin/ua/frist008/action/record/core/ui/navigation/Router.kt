package ua.frist008.action.record.core.ui.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.ShareCompat
import androidx.core.net.toUri
import androidx.navigation3.runtime.NavKey
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.ump.UserMessagingPlatform
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
            is NavCommand.AppLanguageSettings -> openAppLanguageSettings()
            is NavCommand.AppDetailsSettings -> openAppDetailsSettings()
            is NavCommand.AdsPrivacyOptions -> openAdsPrivacyOptions()
            is NavCommand.OpenSourceLicenses -> openOpenSourceLicenses()
            else -> {
                if (command.isReplaceScreen) backStack.removeLastOrNull()
                backStack.add(command)
            }
        }
    }

    private fun back(command: NavCommand.BackCommand) {
        if (command.backToScreen == null) {
            if (backStack.size > 1) {
                backStack.removeLastOrNull()
            } else {
                Timber.d("BackCommand: a repeated back on the last screen is dropped")
            }
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

    private fun openAppLanguageSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val packageUri = Uri.fromParts(PACKAGE_SCHEME, context.packageName, null)

            context.startActivity(Intent(Settings.ACTION_APP_LOCALE_SETTINGS, packageUri))
        } else {
            Timber.d("AppLanguageSettings: Android ${Build.VERSION.SDK_INT} has no app language")
        }
    }

    private fun openAppDetailsSettings() {
        val packageUri = Uri.fromParts(PACKAGE_SCHEME, context.packageName, null)

        context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageUri))
    }

    private fun openAdsPrivacyOptions() {
        val activity = context as? Activity

        if (activity != null) {
            UserMessagingPlatform.showPrivacyOptionsForm(activity) { formError ->
                if (formError != null) Timber.w(formError.message)
            }
        } else {
            Timber.d("AdsPrivacyOptions: no activity to show the form in")
        }
    }

    private fun openOpenSourceLicenses() {
        context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
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

    companion object {

        private const val PACKAGE_SCHEME = "package"
    }
}
