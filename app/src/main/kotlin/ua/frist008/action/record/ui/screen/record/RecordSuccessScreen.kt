package ua.frist008.action.record.ui.screen.record

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ua.frist008.action.record.R
import ua.frist008.action.record.ui.component.DefaultScaffold
import ua.frist008.action.record.ui.entity.RecordSuccessState
import ua.frist008.action.record.ui.theme.color.PreviewPalette

@Preview(
    showBackground = true,
    showSystemUi = true,
    backgroundColor = PreviewPalette.PURPLE_LIGHT_LONG,
)
@Composable
fun RecordSuccessScreen(
    @PreviewParameter(RecordProvider::class) entry: RecordSuccessState,
) {
    DefaultScaffold(titleRes = R.string.record_title_recording_screen) { innerPadding ->
        // TODO SuccessScreen
    }
}

private class RecordProvider : PreviewParameterProvider<RecordSuccessState> {

    override val values = sequenceOf(RecordSuccessState())
}
