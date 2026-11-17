package ua.frist008.action.record.data.network.premium

import android.app.Activity
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PremiumRepositoryImplTest {

    private val purchasesFlow = MutableStateFlow<List<Purchase>?>(null)
    private val networkSource: PremiumNetworkSource = mockk {
        every { purchasesFlow } returns this@PremiumRepositoryImplTest.purchasesFlow
        coEvery { acknowledge(any()) } returns true
    }
    private val repository = PremiumRepositoryImpl(networkSource)

    @Test
    fun `isPremiumFlow is null until Google Play answers`() = runTest {
        assertNull(repository.isPremiumFlow.first())
    }

    @Test
    fun `isPremiumFlow is false when nothing is bought`() = runTest {
        purchasesFlow.emit(emptyList())

        assertEquals(false, repository.isPremiumFlow.first())
    }

    @Test
    fun `isPremiumFlow is true for a paid purchase of premium`() = runTest {
        purchasesFlow.emit(listOf(purchase(state = Purchase.PurchaseState.PURCHASED)))

        assertEquals(true, repository.isPremiumFlow.first())
    }

    @Test
    fun `isPremiumFlow is false while the payment is pending`() = runTest {
        purchasesFlow.emit(listOf(purchase(state = Purchase.PurchaseState.PENDING)))

        assertEquals(false, repository.isPremiumFlow.first())
    }

    @Test
    fun `isPremiumFlow is false for another product`() = runTest {
        val purchase = purchase(
            state = Purchase.PurchaseState.PURCHASED,
            productId = "another",
        )
        purchasesFlow.emit(listOf(purchase))

        assertEquals(false, repository.isPremiumFlow.first())
    }

    @Test
    fun `a paid unconfirmed purchase is confirmed once`() = runTest {
        purchasesFlow.emit(listOf(purchase(state = Purchase.PurchaseState.PURCHASED)))

        repository.isPremiumFlow.first()
        repository.isPremiumFlow.first()

        coVerify(exactly = 1) { networkSource.acknowledge(TOKEN) }
    }

    @Test
    fun `a pending purchase is not confirmed`() = runTest {
        purchasesFlow.emit(listOf(purchase(state = Purchase.PurchaseState.PENDING)))

        repository.isPremiumFlow.first()

        coVerify(exactly = 0) { networkSource.acknowledge(any()) }
    }

    @Test
    fun `a confirmed purchase is not confirmed again`() = runTest {
        val purchase = purchase(state = Purchase.PurchaseState.PURCHASED, isAcknowledged = true)
        purchasesFlow.emit(listOf(purchase))

        repository.isPremiumFlow.first()

        coVerify(exactly = 0) { networkSource.acknowledge(any()) }
    }

    @Test
    fun `getPrice answers the price of the first offer`() = runTest {
        coEvery { networkSource.queryProduct(PremiumRepositoryImpl.PRODUCT_ID) } returns
            product(price = "3,00 $", offerToken = OFFER_TOKEN)

        assertEquals("3,00 $", repository.getPrice())
    }

    @Test
    fun `getPrice is null when Google Play does not sell Premium`() = runTest {
        coEvery { networkSource.queryProduct(PremiumRepositoryImpl.PRODUCT_ID) } returns null

        assertNull(repository.getPrice())
    }

    @Test
    fun `buy opens Google Play with the offer of Premium`() = runTest {
        val activity: Activity = mockk()
        val product = product(price = "3,00 $", offerToken = OFFER_TOKEN)
        coEvery { networkSource.queryProduct(PremiumRepositoryImpl.PRODUCT_ID) } returns product
        coEvery { networkSource.launchPurchase(activity, product, OFFER_TOKEN) } returns true

        assertTrue(repository.buy(activity))
    }

    @Test
    fun `buy answers false when Google Play does not sell Premium`() = runTest {
        coEvery { networkSource.queryProduct(PremiumRepositoryImpl.PRODUCT_ID) } returns null

        assertFalse(repository.buy(mockk()))
    }

    @Test
    fun `refresh asks Google Play for the purchases`() = runTest {
        coEvery { networkSource.queryPurchases() } returns Unit

        repository.refresh()

        coVerify(exactly = 1) { networkSource.queryPurchases() }
    }

    private fun purchase(
        state: Int,
        productId: String = PremiumRepositoryImpl.PRODUCT_ID,
        isAcknowledged: Boolean = false,
    ): Purchase = mockk {
        every { purchaseState } returns state
        every { products } returns listOf(productId)
        every { purchaseToken } returns TOKEN
        every { this@mockk.isAcknowledged } returns isAcknowledged
    }

    private fun product(price: String, offerToken: String): ProductDetails {
        val offer: ProductDetails.OneTimePurchaseOfferDetails = mockk {
            every { formattedPrice } returns price
            every { this@mockk.offerToken } returns offerToken
        }

        return mockk {
            every { oneTimePurchaseOfferDetailsList } returns listOf(offer)
        }
    }

    companion object {

        private const val TOKEN = "token"
        private const val OFFER_TOKEN = "offer"
    }
}
