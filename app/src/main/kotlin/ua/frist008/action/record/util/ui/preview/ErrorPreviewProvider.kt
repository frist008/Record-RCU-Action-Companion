package ua.frist008.action.record.util.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ua.frist008.action.record.ui.entity.base.UIState

class ErrorPreviewProvider : PreviewParameterProvider<UIState.Error> {

    override val values = sequenceOf(UIState.Error())
}
