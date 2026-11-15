package ua.frist008.action.record.core.ui.resource.svg

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.frist008.action.record.core.ui.resource.Icons

val Icons.AdsOff: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "AdsOff",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        // The square with the band of the slash cut out: the frame and the letters part around it
        val gapClip = PathData {
            moveTo(0f, 0f)
            horizontalLineTo(24f)
            verticalLineTo(24f)
            horizontalLineTo(0f)
            close()
            moveTo(4.41f, 1.59f)
            arcTo(2f, 2f, 0f, false, false, 1.59f, 4.41f)
            lineTo(19.59f, 22.41f)
            arcTo(2f, 2f, 0f, false, false, 22.41f, 19.59f)
            close()
        }
        group(clipPathData = gapClip) {
            path(
                stroke = SolidColor(Color(0xFFF1F2F2)),
                strokeLineWidth = 1.6f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
            ) {
                moveTo(4f, 5f)
                horizontalLineTo(20f)
                arcTo(2f, 2f, 0f, false, true, 22f, 7f)
                verticalLineTo(17f)
                arcTo(2f, 2f, 0f, false, true, 20f, 19f)
                horizontalLineTo(4f)
                arcTo(2f, 2f, 0f, false, true, 2f, 17f)
                verticalLineTo(7f)
                arcTo(2f, 2f, 0f, false, true, 4f, 5f)
                close()
                moveTo(6f, 15.5f)
                lineTo(8.5f, 8.5f)
                lineTo(11f, 15.5f)
                moveTo(6.9f, 13f)
                horizontalLineTo(10.1f)
                moveTo(13.5f, 8.5f)
                verticalLineTo(15.5f)
                horizontalLineTo(15f)
                arcTo(3.5f, 3.5f, 0f, false, false, 15f, 8.5f)
                horizontalLineTo(13.5f)
                close()
            }
        }
        path(
            stroke = SolidColor(Color(0xFFF1F2F2)),
            strokeLineWidth = 1.6f,
            strokeLineCap = StrokeCap.Round,
        ) {
            moveTo(3f, 3f)
            lineTo(21f, 21f)
        }
    }.build()
}

@Preview
@Composable
private fun AdsOffPreview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = Icons.AdsOff, contentDescription = null)
    }
}