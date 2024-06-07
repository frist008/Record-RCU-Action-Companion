package ua.frist008.action.record.util.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class BooleanPreviewProvider : PreviewParameterProvider<Boolean> {

    override val values = sequenceOf(true, false)
}
