package ua.frist008.action.record.ui.screen.device

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import ua.frist008.action.record.ui.entity.base.UIState
import ua.frist008.action.record.ui.entity.device.DeviceLoadingState
import ua.frist008.action.record.ui.theme.color.PreviewPalette
import ua.frist008.action.record.util.ui.preview.ErrorPreviewProvider

@Preview(
    showSystemUi = true,
    showBackground = true,
    backgroundColor = PreviewPalette.PURPLE_DARK_LONG,
)
@Composable
fun DevicesErrorScreen(
    @PreviewParameter(ErrorPreviewProvider::class) cause: UIState.Error,
    onSurfaceClick: (DeviceLoadingState) -> Unit = {},
) {
    DevicesLoadingScreen(DeviceLoadingState(""), onSurfaceClick)
}
