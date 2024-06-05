package ua.frist008.action.record.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import ua.frist008.action.record.presentation.impl.DevicesViewModel
import ua.frist008.action.record.ui.entity.base.UIState
import ua.frist008.action.record.ui.entity.device.DeviceProgressState
import ua.frist008.action.record.ui.entity.device.DeviceSuccessState
import ua.frist008.action.record.ui.entity.device.DevicesSuccessState
import ua.frist008.action.record.util.exception.unsupportedUI
import ua.frist008.action.record.util.ui.ExceptionPreviewProvider

@Composable
fun DevicesScreen(viewModel: DevicesViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    when (val currentState = state) {
        is DevicesSuccessState -> SuccessScreen(currentState)
        is UIState.Error -> ErrorScreen(currentState)
        is DeviceProgressState -> LoadingScreen(currentState)

        is UIState.Progress -> {
            // None
        }

        else -> unsupportedUI()
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
private fun SuccessScreen(@PreviewParameter(DevicesProvider::class) list: DevicesSuccessState) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Text(
            text = "Android",
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
private fun ErrorScreen(@PreviewParameter(ExceptionPreviewProvider::class) cause: UIState.Error) {
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
private fun LoadingScreen(@PreviewParameter(DeviceProgressProvider::class) entity: DeviceProgressState) {
}

private class DevicesProvider : PreviewParameterProvider<PersistentList<DeviceSuccessState>> {

    override val values = sequenceOf(
        persistentListOf(
            DeviceSuccessState(
                status = true,
                name = "Name PC",
                address = "192.168.0.1:2555",
            ),
        ),
    )
}

private class DeviceProgressProvider : PreviewParameterProvider<DeviceProgressState> {

    override val values = sequenceOf(DeviceProgressState("5"))
}
