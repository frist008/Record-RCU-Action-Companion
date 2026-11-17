package ua.frist008.action.record.features

import android.app.Activity
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ua.frist008.action.record.data.network.premium.PremiumNetworkSource

/**
 * Google Play Billing, faked at the network layer of the app: the user has bought nothing, so the
 * record screen shows the banner and the way to Premium.
 */
class FakePlay : PremiumNetworkSource {

    private val mutablePurchasesFlow = MutableStateFlow<List<Purchase>?>(emptyList())

    override val purchasesFlow: StateFlow<List<Purchase>?> = mutablePurchasesFlow.asStateFlow()

    override suspend fun queryPurchases() = Unit

    override suspend fun queryProduct(productId: String): ProductDetails? = null

    override suspend fun launchPurchase(
        activity: Activity,
        product: ProductDetails,
        offerToken: String?,
    ): Boolean = false

    override suspend fun acknowledge(purchaseToken: String): Boolean = false
}
