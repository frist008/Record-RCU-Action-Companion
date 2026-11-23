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

/** The ripple of `LocalIndication` is read by the click node itself, with no composition */
fun Modifier.clickable(isRippleEnabled: Boolean, onClick: () -> Unit): Modifier =
    if (isRippleEnabled) {
        this.clickable(onClick = onClick)
    } else {
        this.clickable(interactionSource = null, indication = null, onClick = onClick)
    }

// Shared modifier for all three dots
fun Modifier.dotBehaviour(size: Dp, anchor: Float): Modifier {
    val dotLift = 1.5 // in sizes of the dot
    val dotMaxScale = 1.25f
    val dotMinAlpha = 0.7f

    return this
        .offset { IntOffset(0, (-dotLift * size.toPx() * anchor.dp.toPx()).toInt()) }
        .scale(lerp(1f, dotMaxScale, anchor)) // Size pulse effect
        .alpha(lerp(dotMinAlpha, 1f, anchor)) // Fade-in/out effect
}
