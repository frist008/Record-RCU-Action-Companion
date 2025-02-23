package ua.frist008.action.record.core.ui.component.modifier

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp

fun Modifier.clickable(isRippleEnabled: Boolean, onClick: () -> Unit): Modifier = composed {
    this.clickable(
        indication = if (isRippleEnabled) LocalIndication.current else null,
        interactionSource = remember { MutableInteractionSource() },
    ) {
        onClick()
    }
}

// Shared modifier for all three dots
fun Modifier.dotBehaviour(size: Dp, anchor: Float) =
    this
        .offset { IntOffset(0, (-1.5 * size.toPx() * anchor.dp.toPx()).toInt()) }
        .scale(lerp(1f, 1.25f, anchor)) // Size pulse effect
        .alpha(lerp(0.7f, 1f, anchor)) // Fade-in/out effect
