package ua.frist008.action.record.features.record

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.frist008.action.record.R
import ua.frist008.action.record.core.ui.theme.AppTheme
import ua.frist008.action.record.core.ui.theme.color.PreviewPalette

@Preview(
    showBackground = true,
    showSystemUi = true,
    backgroundColor = PreviewPalette.PURPLE_LIGHT_LONG,
)
@Composable
fun RecordProgressScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.record_connection),
            style = AppTheme.typography.titleLargeBold,
            modifier = Modifier.padding(bottom = 60.dp, top = 45.dp),
        )

        val duration = 3000
        val infiniteTransition = rememberInfiniteTransition()
        val firstColor = infiniteTransition.animateColor(
            initialValue = Color.White,
            targetValue = Color.White.copy(alpha = 0.0f),
            animationSpec = infiniteRepeatable(
                animation = tween(duration),
                initialStartOffset = StartOffset(0),
            ),
        ).value
        val secondAlpha = firstColor.alpha - 0.3f
        val secondColor =
            firstColor.copy(alpha = if (secondAlpha > 0) secondAlpha else (1 + secondAlpha))

        val thirdAlpha = firstColor.alpha - 0.6f
        val thirdColor =
            firstColor.copy(alpha = if (thirdAlpha > 0) thirdAlpha else (1 + thirdAlpha))

        Row {
            Text(
                text = ".",
                style = AppTheme.typography.titleLargeBold,
                color = secondColor,
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = ".",
                style = AppTheme.typography.titleLargeBold,
                color = thirdColor,
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = ".",
                style = AppTheme.typography.titleLargeBold,
                color = firstColor,
            )
        }
    }
}
