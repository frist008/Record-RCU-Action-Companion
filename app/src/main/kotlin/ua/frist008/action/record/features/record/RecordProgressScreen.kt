package ua.frist008.action.record.features.record

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.StartOffsetType
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.frist008.action.record.R
import ua.frist008.action.record.core.ui.component.modifier.dotBehaviour
import ua.frist008.action.record.core.ui.theme.AppTheme
import ua.frist008.action.record.core.ui.theme.color.PreviewPalette
import kotlin.time.Duration.Companion.seconds

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

        val infiniteTransition = rememberInfiniteTransition()
        val size = 7.5.dp
        val durationMs = 0.6.seconds.inWholeMilliseconds.toInt()
        val anchor1 by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMs),
                repeatMode = RepeatMode.Reverse,
            ),
        )

        val anchor2 by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMs),
                initialStartOffset = StartOffset(
                    offsetMillis = durationMs / 2,
                    offsetType = StartOffsetType.Delay,
                ),
                repeatMode = RepeatMode.Reverse,
            ),
        )

        // Third dot animation (500ms delay)
        val anchor3 by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMs),
                initialStartOffset = StartOffset(
                    offsetMillis = durationMs,
                    offsetType = StartOffsetType.Delay,
                ),
                repeatMode = RepeatMode.Reverse,
            ),
        )

        Row(horizontalArrangement = Arrangement.spacedBy(size * 2)) {
            Box(
                modifier = Modifier
                    .dotBehaviour(size, anchor1)
                    .size(size)
                    .clip(CircleShape)
                    .background(Color.White),
            )
            Box(
                modifier = Modifier
                    .dotBehaviour(size, anchor2)
                    .size(size)
                    .clip(CircleShape)
                    .background(Color.White),
            )
            Box(
                modifier = Modifier
                    .dotBehaviour(size, anchor3)
                    .size(size)
                    .clip(CircleShape)
                    .background(Color.White),
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    backgroundColor = PreviewPalette.PURPLE_LIGHT_LONG,
)
@Composable
private fun RecordProgressScreenPreview() {
    RecordProgressScreen()
}
