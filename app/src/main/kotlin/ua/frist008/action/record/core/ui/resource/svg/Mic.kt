package ua.frist008.action.record.core.ui.resource.svg

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.frist008.action.record.core.ui.resource.Icons

val Icons.Mic: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "Mic",
        defaultWidth = 15.dp,
        defaultHeight = 24.dp,
        viewportWidth = 15f,
        viewportHeight = 24f,
    ).apply {
        path(
            fill = SolidColor(Color(0xFFE74C3C)),
            pathFillType = PathFillType.EvenOdd,
        ) {
            moveTo(3.6f, 3.6f)
            curveTo(3.6f, 1.61f, 5.21f, 0f, 7.2f, 0f)
            curveTo(9.19f, 0f, 10.8f, 1.61f, 10.8f, 3.6f)
            verticalLineTo(12f)
            curveTo(10.8f, 13.99f, 9.19f, 15.6f, 7.2f, 15.6f)
            curveTo(5.21f, 15.6f, 3.6f, 13.99f, 3.6f, 12f)
            verticalLineTo(3.6f)
            close()
            moveTo(9f, 3.6f)
            verticalLineTo(12f)
            curveTo(9f, 12.99f, 8.19f, 13.8f, 7.2f, 13.8f)
            curveTo(6.21f, 13.8f, 5.4f, 12.99f, 5.4f, 12f)
            verticalLineTo(3.6f)
            curveTo(5.4f, 2.61f, 6.21f, 1.8f, 7.2f, 1.8f)
            curveTo(8.19f, 1.8f, 9f, 2.61f, 9f, 3.6f)
            close()
        }
        path(fill = SolidColor(Color(0xFFE74C3C))) {
            moveTo(1.8f, 12f)
            curveTo(1.8f, 11.5f, 1.4f, 11.1f, 0.9f, 11.1f)
            curveTo(0.4f, 11.1f, 0f, 11.5f, 0f, 12f)
            curveTo(0f, 13.91f, 0.76f, 15.74f, 2.11f, 17.09f)
            curveTo(3.25f, 18.23f, 4.72f, 18.94f, 6.3f, 19.14f)
            verticalLineTo(22.2f)
            horizontalLineTo(4.5f)
            curveTo(4f, 22.2f, 3.6f, 22.6f, 3.6f, 23.1f)
            curveTo(3.6f, 23.6f, 4f, 24f, 4.5f, 24f)
            horizontalLineTo(9.9f)
            curveTo(10.4f, 24f, 10.8f, 23.6f, 10.8f, 23.1f)
            curveTo(10.8f, 22.6f, 10.4f, 22.2f, 9.9f, 22.2f)
            horizontalLineTo(8.1f)
            verticalLineTo(19.14f)
            curveTo(9.68f, 18.94f, 11.15f, 18.23f, 12.29f, 17.09f)
            curveTo(13.64f, 15.74f, 14.4f, 13.91f, 14.4f, 12f)
            curveTo(14.4f, 11.5f, 14f, 11.1f, 13.5f, 11.1f)
            curveTo(13f, 11.1f, 12.6f, 11.5f, 12.6f, 12f)
            curveTo(12.6f, 13.43f, 12.03f, 14.81f, 11.02f, 15.82f)
            curveTo(10.01f, 16.83f, 8.63f, 17.4f, 7.2f, 17.4f)
            curveTo(5.77f, 17.4f, 4.39f, 16.83f, 3.38f, 15.82f)
            curveTo(2.37f, 14.81f, 1.8f, 13.43f, 1.8f, 12f)
            close()
        }
    }.build()
}

@Preview
@Composable
private fun MicPreview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = Icons.Mic, contentDescription = null)
    }
}
