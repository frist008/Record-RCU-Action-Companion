package ua.frist008.action.record.core.ui.resource.svg

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Pause: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    Builder(
        name = "Pause", defaultWidth = 20.0.dp, defaultHeight = 28.0.dp,
        viewportWidth = 20.0f, viewportHeight = 28.0f,
    ).apply {
        path(
            fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFffffff)),
            strokeLineWidth = 3.0f, strokeLineCap = Round, strokeLineJoin = Miter,
            strokeLineMiter = 4.0f, pathFillType = NonZero,
        ) {
            moveTo(2.0f, 2.0f)
            lineTo(2.0f, 26.0f)
        }
        path(
            fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFffffff)),
            strokeLineWidth = 3.0f, strokeLineCap = Round, strokeLineJoin = Miter,
            strokeLineMiter = 4.0f, pathFillType = NonZero,
        ) {
            moveTo(18.0f, 2.0f)
            lineTo(18.0f, 26.0f)
        }
    }.build()
}
