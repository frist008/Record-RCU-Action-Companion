package ua.frist008.action.record.ui.theme.typography

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle

private val LocalTypography = Typography()

val DisplayLargeStyle: TextStyle = LocalTypography.displayLarge
val DisplayMediumStyle: TextStyle = LocalTypography.displayMedium
val DisplaySmallStyle: TextStyle = LocalTypography.displaySmall
val HeadlineLargeStyle: TextStyle = LocalTypography.headlineLarge
val HeadlineMediumStyle: TextStyle = LocalTypography.headlineMedium
val HeadlineSmallStyle: TextStyle = LocalTypography.headlineSmall
val TitleLargeStyle: TextStyle = LocalTypography.titleLarge
val TitleMediumStyle: TextStyle = LocalTypography.titleMedium
val TitleSmallStyle: TextStyle = LocalTypography.titleSmall
val BodyLargeStyle: TextStyle = LocalTypography.bodyLarge
val BodyMediumStyle: TextStyle = LocalTypography.bodyMedium
val BodySmallStyle: TextStyle = LocalTypography.bodySmall
val LabelLargeStyle: TextStyle = LocalTypography.labelLarge
val LabelMediumStyle: TextStyle = LocalTypography.labelMedium
val LabelSmallStyle: TextStyle = LocalTypography.labelSmall

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
