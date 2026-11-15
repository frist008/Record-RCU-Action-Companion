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

val Icons.Camcorder: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "Camcorder",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            fill = SolidColor(Color(0xFFE74C3C)),
            pathFillType = PathFillType.EvenOdd,
        ) {
            moveTo(16.5f, 13.44f)
            lineTo(21.5f, 16.33f)
            verticalLineTo(7.67f)
            lineTo(16.5f, 10.56f)
            verticalLineTo(6f)
            curveTo(16.5f, 5.72f, 16.28f, 5.5f, 16f, 5.5f)
            horizontalLineTo(4f)
            curveTo(3.72f, 5.5f, 3.5f, 5.72f, 3.5f, 6f)
            verticalLineTo(18f)
            curveTo(3.5f, 18.28f, 3.72f, 18.5f, 4f, 18.5f)
            horizontalLineTo(16f)
            curveTo(16.28f, 18.5f, 16.5f, 18.28f, 16.5f, 18f)
            verticalLineTo(13.44f)
            close()
            moveTo(18f, 6f)
            curveTo(18f, 4.9f, 17.1f, 4f, 16f, 4f)
            horizontalLineTo(4f)
            curveTo(2.9f, 4f, 2f, 4.9f, 2f, 6f)
            verticalLineTo(18f)
            curveTo(2f, 19.1f, 2.9f, 20f, 4f, 20f)
            horizontalLineTo(16f)
            curveTo(17.1f, 20f, 18f, 19.1f, 18f, 18f)
            verticalLineTo(16.04f)
            lineTo(20.75f, 17.63f)
            curveTo(21.75f, 18.21f, 23f, 17.48f, 23f, 16.33f)
            verticalLineTo(7.67f)
            curveTo(23f, 6.52f, 21.75f, 5.79f, 20.75f, 6.37f)
            lineTo(18f, 7.96f)
            verticalLineTo(6f)
            close()
        }
    }.build()
}

@Preview
@Composable
private fun CamcorderPreview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = Icons.Camcorder, contentDescription = null)
    }
}