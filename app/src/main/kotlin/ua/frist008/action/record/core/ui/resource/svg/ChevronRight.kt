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

val Icons.ChevronRight: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "ChevronRight",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
        autoMirror = true,
    ).apply {
        path(fill = SolidColor(Color(0xFFFFFFFF))) {
            moveTo(8.59f, 16.59f)
            lineTo(13.17f, 12f)
            lineTo(8.59f, 7.41f)
            lineTo(10f, 6f)
            lineToRelative(6f, 6f)
            lineToRelative(-6f, 6f)
            lineToRelative(-1.41f, -1.41f)
            close()
        }
    }.build()
}

@Preview
@Composable
private fun ChevronRightPreview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = Icons.ChevronRight, contentDescription = null)
    }
}
