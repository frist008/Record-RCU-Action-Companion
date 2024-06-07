package ua.frist008.action.record.ui.screen.device

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ua.frist008.action.record.ui.entity.device.DeviceProgressState
import ua.frist008.action.record.ui.theme.color.Palette

@Preview(
    showBackground = true,
    backgroundColor = Palette.PURPLE_DARK_LONG,
)
@Composable
fun DevicesLoadingScreen(
    @PreviewParameter(DeviceProgressProvider::class) entity: DeviceProgressState,
    innerPadding: PaddingValues = PaddingValues(),
) {
}

private class DeviceProgressProvider : PreviewParameterProvider<DeviceProgressState> {

    override val values = sequenceOf(DeviceProgressState("5"))
}
