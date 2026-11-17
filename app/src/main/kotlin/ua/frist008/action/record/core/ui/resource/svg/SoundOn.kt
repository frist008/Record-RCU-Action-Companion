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

val Icons.SoundOn: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "SoundOn",
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
            moveTo(18.93f, 12.2f)
            curveTo(18.98f, 10.46f, 18.06f, 8.85f, 16.51f, 8.01f)
            curveTo(16.13f, 7.8f, 15.66f, 7.94f, 15.45f, 8.32f)
            curveTo(15.25f, 8.7f, 15.39f, 9.17f, 15.77f, 9.37f)
            curveTo(16.78f, 9.93f, 17.42f, 11.02f, 17.38f, 12.15f)
            curveTo(17.33f, 13.86f, 15.83f, 14.71f, 15.76f, 14.75f)
            curveTo(15.39f, 14.95f, 15.25f, 15.42f, 15.45f, 15.8f)
            curveTo(15.59f, 16.06f, 15.86f, 16.2f, 16.14f, 16.2f)
            curveTo(16.26f, 16.2f, 16.39f, 16.17f, 16.51f, 16.11f)
            curveTo(16.6f, 16.06f, 18.85f, 14.81f, 18.93f, 12.2f)
            verticalLineTo(12.2f)
            close()
        }
        path(fill = SolidColor(Color(0xFFF1F2F2))) {
            moveTo(18.31f, 4.39f)
            curveTo(17.96f, 4.13f, 17.48f, 4.2f, 17.22f, 4.54f)
            curveTo(16.96f, 4.89f, 17.03f, 5.37f, 17.38f, 5.63f)
            curveTo(17.9f, 6.02f, 20.52f, 8.17f, 20.59f, 11.88f)
            curveTo(20.66f, 15.84f, 17.71f, 18.16f, 17.38f, 18.41f)
            curveTo(17.03f, 18.67f, 16.96f, 19.15f, 17.22f, 19.5f)
            curveTo(17.37f, 19.7f, 17.61f, 19.81f, 17.84f, 19.81f)
            curveTo(18f, 19.81f, 18.17f, 19.76f, 18.31f, 19.65f)
            curveTo(18.71f, 19.35f, 22.23f, 16.58f, 22.15f, 11.85f)
            curveTo(22.07f, 7.42f, 18.94f, 4.86f, 18.31f, 4.39f)
            lineTo(18.31f, 4.39f)
            close()
        }
    }.build()
}

@Preview
@Composable
private fun SoundOnPreview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = Icons.SoundOn, contentDescription = null)
    }
}