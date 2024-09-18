package ua.frist008.action.record.core.ui.resource.svg

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Stop: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    Builder(
        name = "Stop", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
        viewportWidth = 24.0f, viewportHeight = 24.0f,
    ).apply {
        path(
            fill = SolidColor(Color(0xFFffffff)), stroke = null, strokeLineWidth = 0.0f,
            strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
            pathFillType = NonZero,
        ) {
            moveTo(0.0f, 0.0f)
            horizontalLineToRelative(24.0f)
            verticalLineToRelative(24.0f)
            horizontalLineToRelative(-24.0f)
            close()
        }
    }.build()
}
