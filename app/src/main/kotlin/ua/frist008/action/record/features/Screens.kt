package ua.frist008.action.record.features

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Suppress("ConvertObjectToDataObject")
@Immutable
@Serializable
sealed class NavCommand(
    @Transient val isReplaceScreen: Boolean = false,
) {

    @Serializable @Immutable class BackCommand(val backToScreen: NavCommand? = null) : NavCommand()

    @Serializable @Immutable data class Link(val url: String) : NavCommand()

    @Serializable @Immutable object DevicesScreen : NavCommand()
    @Serializable @Immutable data class RecordScreen(val pcId: Long) : NavCommand()
    @Serializable @Immutable object SettingsScreen : NavCommand()
}
