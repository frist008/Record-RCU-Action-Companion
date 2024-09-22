package ua.frist008.action.record.core.ui.theme.typography

import androidx.compose.material3.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import ua.frist008.action.record.core.ui.theme.AppTheme

private val LocalTypography = Typography()

// Display
private val DisplayLargeStyle: TextStyle = LocalTypography.displayLarge.copy(
    color = AppTheme.colors.onBackground,
    fontSize = 96.sp,
)
private val DisplayMediumStyle: TextStyle = LocalTypography.displayMedium.copy(
    color = AppTheme.colors.onBackground,
)
private val DisplaySmallStyle: TextStyle = LocalTypography.displaySmall.copy(
    color = AppTheme.colors.onBackground,
    fontSize = 44.sp,
)

// Headline
private val HeadlineLargeStyle: TextStyle = LocalTypography.headlineLarge.copy(
    color = AppTheme.colors.onBackground,
)
private val HeadlineMediumStyle: TextStyle = LocalTypography.headlineMedium.copy(
    color = AppTheme.colors.onBackground,
)
private val HeadlineSmallStyle: TextStyle = LocalTypography.headlineSmall.copy(
    color = AppTheme.colors.onBackground,
    fontSize = 22.sp,
    fontWeight = FontWeight.Bold,
    lineHeight = 26.sp,
)

// Title
private val TitleLargeStyle: TextStyle = LocalTypography.titleLarge.copy(
    color = AppTheme.colors.onBackground,
    fontSize = 22.sp,
)
private val TitleMediumStyle: TextStyle = LocalTypography.titleMedium.copy(
    color = AppTheme.colors.onBackground,
)
private val TitleSmallStyle: TextStyle = LocalTypography.titleSmall.copy(
    color = AppTheme.colors.onBackground,
)

// Body
private val BodyLargeStyle: TextStyle = LocalTypography.bodyLarge.copy(
    color = AppTheme.colors.onBackground,
)
private val BodyMediumStyle: TextStyle = LocalTypography.bodyMedium.copy(
    color = AppTheme.colors.onBackground,
    fontSize = 14.sp,
)
private val BodySmallStyle: TextStyle = LocalTypography.bodySmall.copy(
    color = AppTheme.colors.onBackground,
)

// Label
// Button -> Text
private val LabelLargeStyle: TextStyle = LocalTypography.labelLarge.copy(
    color = AppTheme.colors.onBackground,
)
private val LabelMediumStyle: TextStyle = LocalTypography.labelMedium.copy(
    color = AppTheme.colors.onBackground,
)
private val LabelSmallStyle: TextStyle = LocalTypography.labelSmall.copy(
    color = AppTheme.colors.onBackground,
)

@Immutable data object AppTypography : () -> Typography {

    private val materialTypography = Typography(
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

    val displayLarge: TextStyle = materialTypography.displayLarge
    val displayMedium: TextStyle = materialTypography.displayMedium
    val displaySmall: TextStyle = materialTypography.displaySmall
    val headlineLarge: TextStyle = materialTypography.headlineLarge
    val headlineMedium: TextStyle = materialTypography.headlineMedium
    val headlineSmall: TextStyle = materialTypography.headlineSmall
    val titleLarge: TextStyle = materialTypography.titleLarge
    val titleMedium: TextStyle = materialTypography.titleMedium
    val titleSmall: TextStyle = materialTypography.titleSmall
    val bodyLarge: TextStyle = materialTypography.bodyLarge
    val bodyMedium: TextStyle = materialTypography.bodyMedium
    val bodySmall: TextStyle = materialTypography.bodySmall
    val labelLarge: TextStyle = materialTypography.labelLarge
    val labelMedium: TextStyle = materialTypography.labelMedium
    val labelSmall: TextStyle = materialTypography.labelSmall

    val link: TextStyle by lazy(LazyThreadSafetyMode.NONE) {
        materialTypography.bodyMedium.merge(
            color = AppTheme.colors.link,
            textDecoration = TextDecoration.Underline,
        )
    }

    override operator fun invoke(): Typography = materialTypography
}
