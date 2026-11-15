package ua.frist008.action.record.features.premium.entity

import androidx.compose.runtime.Immutable
import ua.frist008.action.record.core.ui.UIState

/**
 * The offer of Premium as the screen shows it.
 *
 * @property price the price of the purchase, as Google Play writes it for the user.
 */
@Immutable data class PremiumSuccessState(val price: String) : UIState.Success() {

    companion object {

        fun preview() = PremiumSuccessState(price = "3 $")
    }
}