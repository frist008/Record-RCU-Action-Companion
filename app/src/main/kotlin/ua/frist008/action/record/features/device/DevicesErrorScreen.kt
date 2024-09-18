package ua.frist008.action.record.features.device

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import ua.frist008.action.record.core.ui.UIState
import ua.frist008.action.record.core.ui.component.preview.ErrorPreviewProvider
import ua.frist008.action.record.core.ui.theme.color.PreviewPalette
import ua.frist008.action.record.features.device.entity.DeviceLoadingState

@Preview(
    showSystemUi = true,
    showBackground = true,
    backgroundColor = PreviewPalette.PURPLE_DARK_LONG,
)
@Composable
fun DevicesErrorScreen(
    @PreviewParameter(ErrorPreviewProvider::class) cause: UIState.Error,
    onSurfaceClick: () -> Unit = {},
    onLinkCLick: () -> Unit = {},
) {
    DevicesLoadingScreen(
        state = DeviceLoadingState(""),
        onSurfaceClick = onSurfaceClick,
        onLinkCLick = onLinkCLick,
    )
}
