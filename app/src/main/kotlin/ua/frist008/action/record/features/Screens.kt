package ua.frist008.action.record.features

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable @Immutable
class NewRoot(val newRootScreen: NavCommand) : NavCommand(false)

@Suppress("ConvertObjectToDataObject")
@Immutable
@Serializable
sealed class NavCommand(
    @Transient val isReplaceScreen: Boolean = false,
) : NavKey {

    @Serializable @Immutable class BackCommand(val backToScreen: NavCommand? = null) : NavCommand()

    @Serializable @Immutable data class Link(val url: String) : NavCommand()

    @Serializable @Immutable data class App(val appPackage: String, val url: String) : NavCommand()

    @Serializable @Immutable
    data class Share(val url: String, @get:StringRes val messageRes: Int) : NavCommand()

    /** The screen of Android where the user picks the language of the app, Android 13 and newer */
    @Serializable @Immutable object AppLanguageSettings : NavCommand()

    /** The page of the app in the settings of Android, where the user gives a refused permission */
    @Serializable @Immutable object AppDetailsSettings : NavCommand()

    /** The form of Google where the user changes the consent to ads, where the law asks for one */
    @Serializable @Immutable object AdsPrivacyOptions : NavCommand()

    @Serializable @Immutable object ConnectionScreen : NavCommand()

    @Serializable @Immutable object RecordScreen : NavCommand()

    @Serializable @Immutable object SettingsScreen : NavCommand()

    @Serializable @Immutable object AboutScreen : NavCommand()

    @Serializable @Immutable object PremiumScreen : NavCommand()
}
