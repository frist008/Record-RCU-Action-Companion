package ua.frist008.action.record.core.ui.component.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ua.frist008.action.record.core.ui.UIState

class ErrorPreviewProvider : PreviewParameterProvider<UIState.Error> {

    override val values = sequenceOf(UIState.Error())
}
