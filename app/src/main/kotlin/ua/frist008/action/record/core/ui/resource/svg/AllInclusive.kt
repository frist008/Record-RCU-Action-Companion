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

val Icons.AllInclusive: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "AllInclusive",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(fill = SolidColor(Color(0xFFF1F2F2))) {
            moveTo(18.6f, 6.62f)
            curveToRelative(-1.44f, 0f, -2.8f, 0.56f, -3.77f, 1.53f)
            lineTo(12f, 10.66f)
            lineTo(10.48f, 12f)
            horizontalLineToRelative(0.01f)
            lineTo(7.8f, 14.39f)
            curveToRelative(-0.64f, 0.64f, -1.49f, 0.99f, -2.4f, 0.99f)
            curveToRelative(-1.87f, 0f, -3.39f, -1.51f, -3.39f, -3.38f)
            reflectiveCurveTo(3.53f, 8.62f, 5.4f, 8.62f)
            curveToRelative(0.91f, 0f, 1.76f, 0.35f, 2.44f, 1.03f)
            lineToRelative(1.13f, 1f)
            lineToRelative(1.51f, -1.34f)
            lineTo(9.22f, 8.2f)
            curveTo(8.2f, 7.18f, 6.84f, 6.62f, 5.4f, 6.62f)
            curveTo(2.42f, 6.62f, 0f, 9.04f, 0f, 12f)
            reflectiveCurveToRelative(2.42f, 5.38f, 5.4f, 5.38f)
            curveToRelative(1.44f, 0f, 2.8f, -0.56f, 3.77f, -1.53f)
            lineToRelative(2.83f, -2.5f)
            lineToRelative(0.01f, 0.01f)
            lineTo(13.52f, 12f)
            horizontalLineToRelative(-0.01f)
            lineToRelative(2.69f, -2.39f)
            curveToRelative(0.64f, -0.64f, 1.49f, -0.99f, 2.4f, -0.99f)
            curveToRelative(1.87f, 0f, 3.39f, 1.51f, 3.39f, 3.38f)
            reflectiveCurveToRelative(-1.52f, 3.38f, -3.39f, 3.38f)
            curveToRelative(-0.9f, 0f, -1.76f, -0.35f, -2.44f, -1.03f)
            lineToRelative(-1.14f, -1.01f)
            lineToRelative(-1.51f, 1.34f)
            lineToRelative(1.27f, 1.12f)
            curveToRelative(1.02f, 1.01f, 2.37f, 1.57f, 3.82f, 1.57f)
            curveToRelative(2.98f, 0f, 5.4f, -2.41f, 5.4f, -5.38f)
            reflectiveCurveToRelative(-2.42f, -5.37f, -5.4f, -5.37f)
            close()
        }
    }.build()
}

@Preview
@Composable
private fun AllInclusivePreview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = Icons.AllInclusive, contentDescription = null)
    }
}
