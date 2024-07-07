package ua.frist008.action.record.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.frist008.action.record.ui.theme.AppTheme
import ua.frist008.action.record.util.resource.Dp

@Composable
fun ColoredCircularProgressIndicator(modifier: Modifier = Modifier, @Dp sizeDp: Int) {
    CircularProgressIndicator(
        modifier = modifier.size(sizeDp.dp),
        color = AppTheme.colors.onTertiary,
        trackColor = AppTheme.colors.tertiary,
    )
}
