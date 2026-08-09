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

val Icons.Stop: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "Stop",
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
        path(fill = SolidColor(Color(0xFFFFFFFF))) {
            moveTo(24f, 24f)
            horizontalLineToRelative(24f)
            verticalLineToRelative(24f)
            horizontalLineToRelative(-24f)
            close()
        }
    }.build()
}

@Preview
@Composable
private fun StopPreview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = Icons.Stop, contentDescription = null)
    }
}
