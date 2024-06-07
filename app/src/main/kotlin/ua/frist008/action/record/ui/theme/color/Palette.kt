package ua.frist008.action.record.ui.theme.color

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Immutable object Palette {

    val TRANSPARENT = Color(0x00000000)

    val WHITE_LIGHT = Color(0xFFFFFFFF)
    val WHITE_NEUTRAL = Color(0xB2FFFFFF)
    val WHITE_DARK = Color(0x70FFFFFF)

    val GREEN_LIGHT = Color(0xFF1ABC9C)
    val GREEN_NEUTRAL = Color(0xB21ABC9C)
    val GREEN_DARK = Color(0xFF36685E)
    val GREEN_TRANSPARENT = Color(0x001ABC9C)

    val YELLOW_LIGHT = Color(0xFFF6BD60)

    val RED_LIGHT = Color(0xFFE74C3C)
    val RED_NEUTRAL = Color(0xB2E74C3C)
    val RED_DARK = Color(0xFF89271D)
    val RED_TRANSPARENT = Color(0x00E74C3C)

    val BLUE_LIGHT = Color(0xFF7AEFFF)

    const val PURPLE_DARK_LONG: Long = 0xFF031F33
    val PURPLE_DARK = Color(PURPLE_DARK_LONG)
    const val PURPLE_LIGHT_LONG: Long = 0xFF351E58
    val PURPLE_LIGHT = Color(PURPLE_LIGHT_LONG)
}

@Immutable object BrushPalette {

    val PURPLE = Brush.verticalGradient(
        colors = listOf(Palette.PURPLE_DARK, Palette.PURPLE_LIGHT),
    )
}
