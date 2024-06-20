package ua.frist008.action.record.ui.screen.device

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import ua.frist008.action.record.ui.entity.base.UIState
import ua.frist008.action.record.ui.entity.device.DeviceLoadingState
import ua.frist008.action.record.ui.theme.color.Palette
import ua.frist008.action.record.util.ui.preview.ErrorPreviewProvider

@Preview(
    showSystemUi = true,
    showBackground = true,
    backgroundColor = Palette.PURPLE_DARK_LONG,
)
@Composable
fun DevicesErrorScreen(
    @PreviewParameter(ErrorPreviewProvider::class) cause: UIState.Error,
    innerPadding: PaddingValues = PaddingValues(),
    onSurfaceClick: (DeviceLoadingState) -> Unit = {},
) {
    DevicesLoadingScreen(DeviceLoadingState(""), innerPadding, onSurfaceClick)
}
