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
import ua.frist008.action.record.presentation.impl.RecordViewModel
import ua.frist008.action.record.ui.entity.RecordSuccessState
import ua.frist008.action.record.ui.entity.base.UIState
import ua.frist008.action.record.util.exception.unsupportedUI
import ua.frist008.action.record.util.ui.ExceptionPreviewProvider

@Composable
fun RecordScreen(viewModel: RecordViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    when (val currentState = state) {
        is RecordSuccessState -> SuccessScreen(currentState)
        is UIState.Error -> ErrorScreen(currentState)

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
private fun SuccessScreen(@PreviewParameter(RecordProvider::class) entry: RecordSuccessState) {
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

private class RecordProvider : PreviewParameterProvider<RecordSuccessState> {

    override val values = sequenceOf(RecordSuccessState())
}
