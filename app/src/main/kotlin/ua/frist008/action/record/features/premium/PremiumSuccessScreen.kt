package ua.frist008.action.record.features.premium

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import ua.frist008.action.record.R
import ua.frist008.action.record.core.ui.component.button.ButtonComponent
import ua.frist008.action.record.core.ui.component.modifier.shape
import ua.frist008.action.record.core.ui.resource.Icons
import ua.frist008.action.record.core.ui.resource.svg.AdsOff
import ua.frist008.action.record.core.ui.resource.svg.AllInclusive
import ua.frist008.action.record.core.ui.resource.svg.AppLogo
import ua.frist008.action.record.core.ui.resource.svg.Star
import ua.frist008.action.record.core.ui.theme.AppTheme
import ua.frist008.action.record.core.ui.theme.color.PreviewPalette
import ua.frist008.action.record.features.premium.entity.PremiumSuccessState

/**
 * The offer of Premium: what the one purchase gives, its price from Google Play and the way
 * back to a purchase made before.
 *
 * @param onBuyClick starts the purchase in Google Play.
 * @param onRestoreClick asks Google Play for a purchase made before.
 */
@Composable
fun PremiumSuccessScreen(
    state: PremiumSuccessState,
    innerPadding: PaddingValues = PaddingValues(),
    onBuyClick: () -> Unit = {},
    onRestoreClick: () -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(innerPadding)
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
    ) {
        PremiumHeader()
        PremiumBenefits()
        PremiumPurchase(
            price = state.price,
            onBuyClick = onBuyClick,
            onRestoreClick = onRestoreClick,
        )
    }
}

@Composable
private fun ColumnScope.PremiumHeader() {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxWidth()
            .glows(
                accentColors = AppTheme.colors.glowAccent,
                badgeColors = AppTheme.colors.glowBadge,
            ),
    ) {
        Image(
            imageVector = Icons.AppLogo,
            contentDescription = null,
            modifier = Modifier
                .padding(top = 24.dp)
                .size(96.dp),
        )
    }
    PremiumBadge()
    Text(
        text = stringResource(R.string.premium_headline),
        style = AppTheme.typography.headlineSmall,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
    )
    Text(
        text = stringResource(R.string.premium_subtitle),
        style = AppTheme.typography.bodyMedium,
        color = AppTheme.colors.hint,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp),
    )
}

@Composable
private fun ColumnScope.PremiumBadge() {
    val badgeColor = AppTheme.colors.badge

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 16.dp)
            .heightIn(min = 24.dp)
            .border(width = 1.dp, color = badgeColor, shape = 12.dp.shape)
            .padding(horizontal = 10.dp),
    ) {
        Image(imageVector = Icons.Star, contentDescription = null)
        Text(
            text = stringResource(R.string.premium_badge),
            style = AppTheme.typography.bodySmall,
            color = badgeColor,
        )
    }
}

@Composable
private fun ColumnScope.PremiumBenefits() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, top = 28.dp, end = 8.dp),
    ) {
        BenefitRow(
            icon = Icons.AdsOff,
            title = stringResource(R.string.premium_no_ads_title),
            summary = stringResource(R.string.premium_no_ads_summary),
        )
        BenefitRow(
            icon = Icons.AllInclusive,
            title = stringResource(R.string.premium_forever_title),
            summary = stringResource(R.string.premium_forever_summary),
        )
    }
}

@Composable
private fun ColumnScope.BenefitRow(icon: ImageVector, title: String, summary: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(44.dp)
                .background(color = AppTheme.colors.accent, shape = 14.dp.shape),
        ) {
            Image(imageVector = icon, contentDescription = null)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = AppTheme.typography.bodyLargeBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = summary,
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colors.hint,
            )
        }
    }
}

@Composable
private fun ColumnScope.PremiumPurchase(
    price: String,
    onBuyClick: () -> Unit,
    onRestoreClick: () -> Unit,
) {
    val buttonShape = 24.dp.shape

    ButtonComponent(
        text = stringResource(R.string.premium_buy_pattern, price),
        style = AppTheme.typography.bodyLargeBold,
        colors = AppTheme.colors.accentButtonColors,
        shape = buttonShape,
        onClick = onBuyClick,
        modifier = Modifier
            .padding(start = 8.dp, top = 24.dp, end = 8.dp)
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .dropShadow(
                shape = buttonShape,
                shadow = Shadow(
                    radius = 16.dp,
                    color = AppTheme.colors.accentShadow,
                    offset = DpOffset(x = 0.dp, y = 4.dp),
                ),
            ),
    )
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, top = 12.dp, end = 8.dp),
    ) {
        Text(
            text = stringResource(R.string.premium_restore),
            style = AppTheme.typography.link,
            modifier = Modifier
                .weight(1f)
                .clickable(role = Role.Button, onClick = onRestoreClick),
        )
        Text(
            text = stringResource(R.string.premium_via_play),
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colors.hintDim,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f),
        )
    }
}

/**
 * Draws the two glows of the design behind the logo; they run past the top of the content, under
 * the app bar.
 */
private fun Modifier.glows(accentColors: List<Color>, badgeColors: List<Color>): Modifier =
    drawWithCache {
        val accentGlowCenterY = 102.dp // the center of the purple glow, from the top of the content
        val accentGlowRadius = 120.dp
        val badgeGlowCenterY = 82.dp // the center of the badge glow, from the top of the content
        val badgeGlowRadius = 75.dp
        val centerX = size.width / 2
        val accentCenter = Offset(centerX, accentGlowCenterY.toPx())
        val accentRadius = accentGlowRadius.toPx()
        val accentBrush = Brush.radialGradient(accentColors, accentCenter, accentRadius)
        val badgeCenter = Offset(centerX, badgeGlowCenterY.toPx())
        val badgeRadius = badgeGlowRadius.toPx()
        val badgeBrush = Brush.radialGradient(badgeColors, badgeCenter, badgeRadius)

        onDrawBehind {
            // The purple glow is an ellipse, 300 dp wide and 240 dp high
            scale(scaleX = 1.25f, scaleY = 1f, pivot = accentCenter) {
                drawCircle(brush = accentBrush, radius = accentRadius, center = accentCenter)
            }
            drawCircle(brush = badgeBrush, radius = badgeRadius, center = badgeCenter)
        }
    }

@Preview(
    showSystemUi = true,
    showBackground = true,
    backgroundColor = PreviewPalette.PURPLE_LIGHT_LONG,
)
@Composable
private fun PremiumSuccessScreenPreview() {
    PremiumSuccessScreen(state = PremiumSuccessState.preview())
}
