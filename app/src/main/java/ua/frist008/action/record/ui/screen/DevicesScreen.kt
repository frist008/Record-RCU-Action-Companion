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
import ua.frist008.action.record.presentation.impl.DevicesViewModel
import ua.frist008.action.record.ui.entity.DeviceUIEntity
import ua.frist008.action.record.ui.entity.base.UIState
import ua.frist008.action.record.util.ui.ExceptionPreviewProvider

@Composable
fun DevicesScreen(viewModel: DevicesViewModel = hiltViewModel()) {
    val state: UIState<DeviceUIEntity, Exception> by viewModel.state.collectAsState()
    val currentState = state
    when {
        currentState.entity != null -> SuccessScreen(currentState.entity)
        currentState.cause != null -> ErrorScreen(currentState.cause)
        else -> LoadingScreen()
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
private fun SuccessScreen(@PreviewParameter(DeviceProvider::class) entry: DeviceUIEntity) {
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
private fun ErrorScreen(@PreviewParameter(ExceptionPreviewProvider::class) cause: Exception) {
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
private fun LoadingScreen() {
}

private class DeviceProvider : PreviewParameterProvider<DeviceUIEntity> {

    override val values = sequenceOf(DeviceUIEntity())
}
