package ua.frist008.action.record.core.ui.component.image

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
import ua.frist008.action.record.core.ui.component.preview.BooleanPreviewProvider
import ua.frist008.action.record.core.ui.theme.AppTheme

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
            .background(if (available) AppTheme.colors.online else AppTheme.colors.offline),
    )
}
