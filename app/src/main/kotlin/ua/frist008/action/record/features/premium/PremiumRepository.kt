package ua.frist008.action.record.features.premium

import android.app.Activity
import kotlinx.coroutines.flow.Flow

interface PremiumRepository {

    /** Whether the user owns Premium: null until Google Play answers, then again on every change */
    val isPremiumFlow: Flow<Boolean?>

    /**
     * Reads the price of Premium from Google Play.
     *
     * @return the price as Google Play writes it for the user, or null when Google Play does not
     * sell it now.
     */
    suspend fun getPrice(): String?

    /**
     * Opens the purchase of Premium in the window of Google Play; the purchase itself comes into
     * [isPremiumFlow].
     *
     * @param activity the screen the window of Google Play opens over; it is not kept.
     * @return true when Google Play opened its window.
     */
    suspend fun buy(activity: Activity): Boolean

    /** Asks Google Play again for the purchases of the user, made on this phone or another one */
    suspend fun refresh()
}
