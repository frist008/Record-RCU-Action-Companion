package ua.frist008.action.record.core.ui.resource.svg

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val Icons.Pause: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "Pause",
        defaultWidth = 71.dp,
        defaultHeight = 71.dp,
        viewportWidth = 71f,
        viewportHeight = 71f,
    ).apply {
        path(
            stroke = SolidColor(Color(0xFFFFFFFF)),
            strokeLineWidth = 2f,
        ) {
            moveTo(35.5f, 35.5f)
            moveToRelative(-34.5f, 0f)
            arcToRelative(34.5f, 34.5f, 0f, isMoreThanHalf = true, isPositiveArc = true, 69f, 0f)
            arcToRelative(34.5f, 34.5f, 0f, isMoreThanHalf = true, isPositiveArc = true, -69f, 0f)
        }
        path(
            stroke = SolidColor(Color(0xFFFFFFFF)),
            strokeLineWidth = 3f,
            strokeLineCap = StrokeCap.Round,
        ) {
            moveTo(27f, 23f)
            lineTo(27f, 47f)
        }
        path(
            stroke = SolidColor(Color(0xFFFFFFFF)),
            strokeLineWidth = 3f,
            strokeLineCap = StrokeCap.Round,
        ) {
            moveTo(43f, 23f)
            lineTo(43f, 47f)
        }
    }.build()
}

@Preview
@Composable
private fun PausePreview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = Icons.Pause, contentDescription = null)
    }
}
