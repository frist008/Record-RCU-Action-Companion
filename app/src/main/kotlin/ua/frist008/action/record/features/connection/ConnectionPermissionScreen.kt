package ua.frist008.action.record.features.connection

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.frist008.action.record.R
import ua.frist008.action.record.core.ui.component.button.ButtonComponent
import ua.frist008.action.record.core.ui.resource.Icons
import ua.frist008.action.record.core.ui.resource.svg.DevicesNotAvailable
import ua.frist008.action.record.core.ui.theme.AppTheme
import ua.frist008.action.record.core.ui.theme.color.PreviewPalette
import ua.frist008.action.record.features.connection.entity.ConnectionPermissionState

/**
 * The search for the PC waiting for the access to the local network: why it is needed and the
 * one step that gives it.
 *
 * @param onContinueClick asks Android for the access, while the user has not refused it.
 * @param onSettingsClick opens the page of the app in the settings of Android, once they have.
 */
@Composable
fun ConnectionPermissionScreen(
    state: ConnectionPermissionState,
    onContinueClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
) {
    val isDenied = state.isDenied

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
            .safeContentPadding(),
    ) {
        ConnectionPermissionHeader(isDenied = isDenied)
        ConnectionPermissionButton(
            isDenied = isDenied,
            onContinueClick = onContinueClick,
            onSettingsClick = onSettingsClick,
        )
        // Keeps the place of the link of the progress screen; the button stands where its timer is
        Spacer(modifier = Modifier.height(60.dp))
    }
}

@Composable
private fun ColumnScope.ConnectionPermissionHeader(isDenied: Boolean) {
    val textRes = if (isDenied) {
        R.string.device_permission_local_network_denied
    } else {
        R.string.device_permission_local_network
    }
    val text = stringResource(textRes)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = text,
            style = AppTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 60.dp),
        )
        Image(
            imageVector = Icons.DevicesNotAvailable,
            contentDescription = text,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(top = 56.dp),
        )
    }
}

@Composable
private fun ColumnScope.ConnectionPermissionButton(
    isDenied: Boolean,
    onContinueClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    val textRes = if (isDenied) {
        R.string.device_permission_settings
    } else {
        R.string.device_permission_continue
    }

    ButtonComponent(
        text = stringResource(textRes),
        style = AppTheme.typography.labelLargeBold,
        colors = AppTheme.colors.accentButtonColors,
        onClick = if (isDenied) onSettingsClick else onContinueClick,
    )
}

@Preview(
    showSystemUi = true,
    showBackground = true,
    backgroundColor = PreviewPalette.PURPLE_LIGHT_LONG,
)
@Composable
private fun ConnectionPermissionScreenAskPreview() {
    ConnectionPermissionScreen(ConnectionPermissionState(isDenied = false))
}

@Preview(
    showSystemUi = true,
    showBackground = true,
    backgroundColor = PreviewPalette.PURPLE_LIGHT_LONG,
)
@Composable
private fun ConnectionPermissionScreenDeniedPreview() {
    ConnectionPermissionScreen(ConnectionPermissionState(isDenied = true))
}
