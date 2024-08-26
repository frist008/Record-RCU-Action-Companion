package ua.frist008.action.record.ui.navigation

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Suppress("ConvertObjectToDataObject")
@Immutable
@Serializable
sealed class NavCommand(
    @Transient val isReplaceScreen: Boolean = false,
) {

    @Immutable class BackCommand(val backToScreen: NavCommand? = null) : NavCommand()

    @Immutable data class Link(val url: String) : NavCommand()

    @Serializable @Immutable
    class NewRoot(val newRootScreen: NavCommand) : NavCommand(false)

    @Serializable @Immutable object DevicesScreen : NavCommand()
    @Serializable @Immutable data class RecordScreen(val pcId: Long) : NavCommand()
    @Serializable @Immutable object SettingsScreen : NavCommand()

    companion object {

        const val ROOT_LAYER = "ROOT_LAYER"
    }
}
