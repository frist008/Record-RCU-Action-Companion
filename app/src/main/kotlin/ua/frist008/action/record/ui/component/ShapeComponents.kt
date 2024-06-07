package ua.frist008.action.record.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ua.frist008.action.record.ui.theme.color.Palette
import ua.frist008.action.record.util.ui.preview.BooleanPreviewProvider

@Preview
@Composable
fun CircleStatusComponent(
    @PreviewParameter(BooleanPreviewProvider::class) available: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(20.dp)
            .clip(CircleShape)
            .background(if (available) Palette.GREEN_LIGHT else Palette.RED_LIGHT),
    )
}
