package ua.frist008.action.record.core.ui.resource.svg

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.frist008.action.record.core.ui.resource.Icons

val Icons.SoundOff: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "SoundOff",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(fill = SolidColor(Color(0xFFF1F2F2))) {
            moveTo(13.65f, 0.08f)
            curveTo(13.23f, -0.09f, 12.76f, 0.02f, 12.45f, 0.35f)
            lineTo(6.17f, 7.17f)
            horizontalLineTo(3.4f)
            curveTo(2.63f, 7.17f, 2f, 7.8f, 2f, 8.57f)
            verticalLineTo(15.38f)
            curveTo(2f, 16.15f, 2.63f, 16.78f, 3.4f, 16.78f)
            horizontalLineTo(6.06f)
            lineTo(12.46f, 23.66f)
            curveTo(12.67f, 23.88f, 12.96f, 24f, 13.25f, 24f)
            curveTo(13.38f, 24f, 13.52f, 23.98f, 13.65f, 23.93f)
            curveTo(14.07f, 23.76f, 14.34f, 23.37f, 14.34f, 22.92f)
            verticalLineTo(1.09f)
            curveTo(14.34f, 0.64f, 14.07f, 0.24f, 13.65f, 0.08f)
            close()
            moveTo(3.55f, 8.73f)
            horizontalLineTo(5.42f)
            verticalLineTo(15.23f)
            horizontalLineTo(3.55f)
            verticalLineTo(8.73f)
            close()
            moveTo(12.79f, 21.73f)
            lineTo(6.97f, 15.49f)
            verticalLineTo(8.6f)
            lineTo(12.79f, 2.28f)
            verticalLineTo(21.73f)
            verticalLineTo(21.73f)
            close()
        }
        path(fill = SolidColor(Color(0xFFF1F2F2))) {
            moveTo(19.94f, 12f)
            lineTo(21.8f, 10.14f)
            curveTo(22.06f, 9.88f, 22.06f, 9.46f, 21.8f, 9.2f)
            curveTo(21.54f, 8.93f, 21.12f, 8.93f, 20.85f, 9.2f)
            lineTo(19f, 11.05f)
            lineTo(17.14f, 9.2f)
            curveTo(16.88f, 8.94f, 16.46f, 8.94f, 16.2f, 9.2f)
            curveTo(15.94f, 9.46f, 15.94f, 9.88f, 16.2f, 10.14f)
            lineTo(18.05f, 12f)
            lineTo(16.2f, 13.86f)
            curveTo(15.93f, 14.12f, 15.93f, 14.54f, 16.2f, 14.8f)
            curveTo(16.33f, 14.93f, 16.5f, 15f, 16.67f, 15f)
            curveTo(16.84f, 15f, 17.01f, 14.93f, 17.14f, 14.8f)
            lineTo(19f, 12.95f)
            lineTo(20.86f, 14.8f)
            curveTo(20.99f, 14.93f, 21.16f, 15f, 21.33f, 15f)
            curveTo(21.5f, 15f, 21.67f, 14.93f, 21.8f, 14.8f)
            curveTo(22.07f, 14.54f, 22.07f, 14.12f, 21.8f, 13.86f)
            lineTo(19.95f, 12f)
            horizontalLineTo(19.94f)
            close()
        }
    }.build()
}

@Preview
@Composable
private fun SoundOffPreview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = Icons.SoundOff, contentDescription = null)
    }
}
