package ua.frist008.action.record.ui.theme.typography

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import ua.frist008.action.record.ui.theme.color.AppColorScheme
import ua.frist008.action.record.ui.theme.color.Palette

private val LocalTypography = Typography()

// Display
val DisplayLargeStyle: TextStyle = LocalTypography.displayLarge.copy(
    color = AppColorScheme.onBackground,
)
val DisplayMediumStyle: TextStyle = LocalTypography.displayMedium.copy(
    color = AppColorScheme.onBackground,
)
val DisplaySmallStyle: TextStyle = LocalTypography.displaySmall.copy(
    color = AppColorScheme.onBackground,
)

// Headline
val HeadlineLargeStyle: TextStyle = LocalTypography.headlineLarge.copy(
    color = AppColorScheme.onBackground,
)
val HeadlineMediumStyle: TextStyle = LocalTypography.headlineMedium.copy(
    color = AppColorScheme.onBackground,
)
val HeadlineSmallStyle: TextStyle = LocalTypography.headlineSmall.copy(
    color = AppColorScheme.onBackground,
    fontSize = 22.sp,
    fontWeight = FontWeight.Bold,
    lineHeight = 26.sp,
)

// Title
val TitleLargeStyle: TextStyle = LocalTypography.titleLarge.copy(
    color = AppColorScheme.onBackground,
    fontSize = 22.sp,
)
val TitleMediumStyle: TextStyle = LocalTypography.titleMedium.copy(
    color = AppColorScheme.onBackground,
)
val TitleSmallStyle: TextStyle = LocalTypography.titleSmall.copy(
    color = AppColorScheme.onBackground,
)

// Body
val BodyLargeStyle: TextStyle = LocalTypography.bodyLarge.copy(
    color = AppColorScheme.onBackground,
)
val BodyMediumStyle: TextStyle = LocalTypography.bodyMedium.copy(
    color = AppColorScheme.onBackground,
    fontSize = 14.sp,
)
val BodySmallStyle: TextStyle = LocalTypography.bodySmall.copy(
    color = AppColorScheme.onBackground,
)

// Label
val LabelLargeStyle: TextStyle = LocalTypography.labelLarge.copy(
    color = AppColorScheme.onBackground,
)
val LabelMediumStyle: TextStyle = LocalTypography.labelMedium.copy(
    color = AppColorScheme.onBackground,
)
val LabelSmallStyle: TextStyle = LocalTypography.labelSmall.copy(
    color = AppColorScheme.onBackground,
)

val Typography = Typography(
    displayLarge = DisplayLargeStyle,
    displayMedium = DisplayMediumStyle,
    displaySmall = DisplaySmallStyle,
    headlineLarge = HeadlineLargeStyle,
    headlineMedium = HeadlineMediumStyle,
    headlineSmall = HeadlineSmallStyle,
    titleLarge = TitleLargeStyle,
    titleMedium = TitleMediumStyle,
    titleSmall = TitleSmallStyle,
    bodyLarge = BodyLargeStyle,
    bodyMedium = BodyMediumStyle,
    bodySmall = BodySmallStyle,
    labelLarge = LabelLargeStyle,
    labelMedium = LabelMediumStyle,
    labelSmall = LabelSmallStyle,
)

val Typography.link: TextStyle by lazy(LazyThreadSafetyMode.NONE) {
    Typography.bodyMedium.merge(
        color = Palette.BLUE_LIGHT,
        textDecoration = TextDecoration.Underline,
    )
}
