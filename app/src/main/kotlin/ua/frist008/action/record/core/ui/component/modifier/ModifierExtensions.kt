package ua.frist008.action.record.core.ui.component.modifier

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp

/** How high a dot rises at the top of its jump, in its own sizes */
private const val DotLift = 1.5

/** The scale of a dot at the top of its jump */
private const val DotMaxScale = 1.25f

/** The alpha of a dot at rest */
private const val DotMinAlpha = 0.7f

/** The ripple of `LocalIndication` is read by the click node itself, with no composition */
fun Modifier.clickable(isRippleEnabled: Boolean, onClick: () -> Unit): Modifier =
    if (isRippleEnabled) {
        this.clickable(onClick = onClick)
    } else {
        this.clickable(interactionSource = null, indication = null, onClick = onClick)
    }

// Shared modifier for all three dots
fun Modifier.dotBehaviour(size: Dp, anchor: Float) =
    this
        .offset { IntOffset(0, (-DotLift * size.toPx() * anchor.dp.toPx()).toInt()) }
        .scale(lerp(1f, DotMaxScale, anchor)) // Size pulse effect
        .alpha(lerp(DotMinAlpha, 1f, anchor)) // Fade-in/out effect
