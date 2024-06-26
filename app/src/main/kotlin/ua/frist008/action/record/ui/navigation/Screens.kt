package ua.frist008.action.record.ui.navigation

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Suppress("ConvertObjectToDataObject")
@Serializable
sealed class NavCommand(
    @Transient val replaceScreen: Boolean = false,
) {

    class BackCommand(val screen: NavCommand? = null) : NavCommand()

    @Serializable object DevicesScreen : NavCommand()
    @Serializable data class RecordScreen(val pcId: Long) : NavCommand()
    @Serializable object SettingsScreen : NavCommand()

    companion object {

        const val ROOT_LAYER = "ROOT_LAYER"
    }
}
