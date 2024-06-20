package ua.frist008.action.record.util.extension.ui

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

fun Modifier.clickable(isRippleEnabled: Boolean, onClick: () -> Unit): Modifier = composed {
    this.clickable(
        indication = if (isRippleEnabled) LocalIndication.current else null,
        interactionSource = remember { MutableInteractionSource() },
    ) {
        onClick()
    }
}
