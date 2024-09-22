package ua.frist008.action.record.core.ui.resource.svg

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val Icons.PlayBold: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "Play Bold",
        defaultWidth = 71.dp,
        defaultHeight = 71.dp,
        viewportWidth = 71f,
        viewportHeight = 71f,
    ).apply {
        path(
            stroke = SolidColor(Color(0xFFFFFFFF)),
            strokeLineWidth = 4f,
        ) {
            moveTo(35.5f, 35.5f)
            moveToRelative(-33.5f, 0f)
            arcToRelative(33.5f, 33.5f, 0f, isMoreThanHalf = true, isPositiveArc = true, 67f, 0f)
            arcToRelative(33.5f, 33.5f, 0f, isMoreThanHalf = true, isPositiveArc = true, -67f, 0f)
        }
        path(fill = SolidColor(Color(0xFFFFFFFF))) {
            moveTo(27f, 48f)
            verticalLineTo(24f)
            lineTo(51f, 36f)
            lineTo(27f, 48f)
            close()
        }
    }.build()
}

@Preview
@Composable
private fun PlayBoldPreview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = Icons.PlayBold, contentDescription = null)
    }
}
