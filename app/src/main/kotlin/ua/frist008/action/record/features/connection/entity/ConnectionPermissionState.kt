package ua.frist008.action.record.features.connection.entity

import androidx.compose.runtime.Immutable
import ua.frist008.action.record.core.ui.UIState

/**
 * The search waits for the access to the local network, which Android 17 and newer ask the user
 * for.
 *
 * @property isDenied whether the user refused it, so only the settings of Android can give it now.
 */
@Immutable data class ConnectionPermissionState(val isDenied: Boolean) : UIState.Error()
