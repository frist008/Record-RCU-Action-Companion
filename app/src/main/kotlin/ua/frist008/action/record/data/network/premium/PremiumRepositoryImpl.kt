package ua.frist008.action.record.data.network.premium

import android.app.Activity
import com.android.billingclient.api.Purchase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ua.frist008.action.record.features.premium.PremiumRepository
import javax.inject.Inject

/**
 * Premium is the one-time product [PRODUCT_ID] of Google Play. A purchase of it counts once it is
 * paid, and is confirmed to Google Play the first time it is seen, as Google Play refunds one left
 * unconfirmed for three days.
 */
class PremiumRepositoryImpl @Inject constructor(
    private val premiumNetworkSource: PremiumNetworkSource,
) : PremiumRepository {

    private val acknowledgeMutex = Mutex()

    /** The purchases confirmed in this process; guarded by [acknowledgeMutex] */
    private val acknowledgedTokens = HashSet<String>()

    override val isPremiumFlow: Flow<Boolean?> = premiumNetworkSource.purchasesFlow
        .onEach { purchases -> acknowledge(purchases.orEmpty()) }
        .map { purchases -> purchases?.any(::isPremium) }
        .distinctUntilChanged()

    override suspend fun getPrice(): String? =
        premiumNetworkSource.queryProduct(PRODUCT_ID)
            ?.oneTimePurchaseOfferDetailsList
            ?.firstOrNull()
            ?.formattedPrice

    override suspend fun buy(activity: Activity): Boolean {
        val product = premiumNetworkSource.queryProduct(PRODUCT_ID) ?: return false
        val offerToken = product.oneTimePurchaseOfferDetailsList?.firstOrNull()?.offerToken

        return premiumNetworkSource.launchPurchase(activity, product, offerToken)
    }

    override suspend fun refresh() {
        premiumNetworkSource.queryPurchases()
    }

    private suspend fun acknowledge(purchases: List<Purchase>) {
        val unconfirmed = purchases.filter { purchase ->
            isPremium(purchase) && !purchase.isAcknowledged
        }

        acknowledgeMutex.withLock {
            for (purchase in unconfirmed) {
                val token = purchase.purchaseToken

                if (token !in acknowledgedTokens && premiumNetworkSource.acknowledge(token)) {
                    acknowledgedTokens += token
                }
            }
        }
    }

    private fun isPremium(purchase: Purchase): Boolean {
        val isPaid = purchase.purchaseState == Purchase.PurchaseState.PURCHASED

        return isPaid && PRODUCT_ID in purchase.products
    }

    companion object {

        /** The id of the product in Play Console */
        const val PRODUCT_ID = "premium"
    }
}
