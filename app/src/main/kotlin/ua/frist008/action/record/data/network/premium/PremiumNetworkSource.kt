package ua.frist008.action.record.data.network.premium

import android.app.Activity
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import kotlinx.coroutines.flow.StateFlow

interface PremiumNetworkSource {

    /**
     * The one-time purchases of the app Google Play holds for the user: null until it answers, then
     * again on every purchase.
     */
    val purchasesFlow: StateFlow<List<Purchase>?>

    /**
     * Asks Google Play for the one-time purchases of the user into [purchasesFlow]; where Google
     * Play cannot answer, a flow that has no answer yet gets an empty list.
     */
    suspend fun queryPurchases()

    /**
     * Reads a one-time product of the app from Google Play.
     *
     * @return the product, or null when Google Play does not sell it now.
     */
    suspend fun queryProduct(productId: String): ProductDetails?

    /**
     * Opens the window of Google Play that buys [product].
     *
     * @param offerToken the offer of the product, null for a product that has none of its own.
     * @return true when Google Play opened its window.
     */
    suspend fun launchPurchase(
        activity: Activity,
        product: ProductDetails,
        offerToken: String?,
    ): Boolean

    /**
     * Confirms a purchase to Google Play, which refunds one left unconfirmed for three days.
     *
     * @return true when Google Play took the confirmation.
     */
    suspend fun acknowledge(purchaseToken: String): Boolean
}