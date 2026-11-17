package ua.frist008.action.record.data.network.premium

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.acknowledgePurchase
import com.android.billingclient.api.queryProductDetails
import com.android.billingclient.api.queryPurchasesAsync
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import timber.log.Timber
import ua.frist008.action.record.core.di.qualifier.concurrent.Main
import javax.inject.Inject
import kotlin.coroutines.resume

/**
 * Google Play Billing: one client for the process, connected on the first call and reconnected
 * by the library itself after that.
 */
class PremiumNetworkSourceImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    @param:Main private val mainDispatcher: CoroutineDispatcher,
) : PremiumNetworkSource {

    private val mutablePurchasesFlow = MutableStateFlow<List<Purchase>?>(null)

    override val purchasesFlow: StateFlow<List<Purchase>?> = mutablePurchasesFlow.asStateFlow()

    private val connectionMutex = Mutex()

    private val billingClient: BillingClient by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        val pendingPurchasesParams = PendingPurchasesParams.newBuilder()
            .enableOneTimeProducts()
            .build()

        BillingClient.newBuilder(context)
            .setListener(::onPurchasesUpdated)
            .enablePendingPurchases(pendingPurchasesParams)
            .enableAutoServiceReconnection()
            .build()
    }

    override suspend fun queryPurchases() {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()
        val result = if (connect()) billingClient.queryPurchasesAsync(params) else null
        val billingResult = result?.billingResult

        if (result != null && billingResult?.responseCode == BillingResponseCode.OK) {
            mutablePurchasesFlow.emit(result.purchasesList)
        } else {
            Timber.w("queryPurchases: %s", billingResult?.debugMessage)
            mutablePurchasesFlow.update { purchases -> purchases ?: emptyList() }
        }
    }

    override suspend fun queryProduct(productId: String): ProductDetails? {
        if (!connect()) return null

        val product = QueryProductDetailsParams.Product.newBuilder()
            .setProductId(productId)
            .setProductType(BillingClient.ProductType.INAPP)
            .build()
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(listOf(product))
            .build()
        val result = billingClient.queryProductDetails(params)
        val productDetails = result.productDetailsList?.firstOrNull()

        if (productDetails == null) {
            Timber.w("queryProduct %s: %s", productId, result.billingResult.debugMessage)
        }

        return productDetails
    }

    override suspend fun launchPurchase(
        activity: Activity,
        product: ProductDetails,
        offerToken: String?,
    ): Boolean {
        if (!connect()) return false

        val productParams = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(product)
            .apply { if (offerToken != null) setOfferToken(offerToken) }
            .build()
        val params = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(listOf(productParams))
            .build()
        val billingResult = withContext(mainDispatcher) {
            billingClient.launchBillingFlow(activity, params)
        }
        val isLaunched = billingResult.responseCode == BillingResponseCode.OK

        if (!isLaunched) Timber.w("launchPurchase: %s", billingResult.debugMessage)

        return isLaunched
    }

    override suspend fun acknowledge(purchaseToken: String): Boolean {
        if (!connect()) return false

        val params = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchaseToken)
            .build()
        val billingResult = billingClient.acknowledgePurchase(params)
        val isAcknowledged = billingResult.responseCode == BillingResponseCode.OK

        if (!isAcknowledged) Timber.w("acknowledge: %s", billingResult.debugMessage)

        return isAcknowledged
    }

    /**
     * Connects the client once; the library reconnects it by itself after that.
     *
     * @return true when Google Play answers; false where it does not, as on a phone without it.
     */
    private suspend fun connect(): Boolean = connectionMutex.withLock {
        if (billingClient.isReady) return@withLock true

        val billingResult = withContext(mainDispatcher) {
            suspendCancellableCoroutine { continuation ->
                billingClient.startConnection(object : BillingClientStateListener {
                    override fun onBillingSetupFinished(billingResult: BillingResult) {
                        if (continuation.isActive) continuation.resume(billingResult)
                    }

                    override fun onBillingServiceDisconnected() {
                        Timber.d("Google Play Billing disconnected")
                    }
                })
            }
        }
        val isConnected = billingResult.responseCode == BillingResponseCode.OK

        if (!isConnected) Timber.w("connect: %s", billingResult.debugMessage)

        isConnected
    }

    /** Takes a purchase made in the window of Google Play in place of its older copy */
    private fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        val responseCode = billingResult.responseCode

        if (responseCode == BillingResponseCode.OK && purchases != null) {
            val newTokens = purchases.map { newPurchase -> newPurchase.purchaseToken }.toSet()

            mutablePurchasesFlow.update { oldPurchases ->
                oldPurchases.orEmpty().filterNot { oldPurchase ->
                    oldPurchase.purchaseToken in newTokens
                } + purchases
            }
        } else {
            Timber.d("onPurchasesUpdated %d: %s", responseCode, billingResult.debugMessage)
        }
    }
}
