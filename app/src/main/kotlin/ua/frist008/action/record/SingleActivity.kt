package ua.frist008.action.record

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.ads.mobile.sdk.MobileAds
import com.google.android.libraries.ads.mobile.sdk.common.AgeRestrictedTreatment
import com.google.android.libraries.ads.mobile.sdk.common.RequestConfiguration
import com.google.android.libraries.ads.mobile.sdk.common.RequestConfiguration.MaxAdContentRating
import com.google.android.libraries.ads.mobile.sdk.initialization.InitializationConfig
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import timber.log.Timber
import ua.frist008.action.record.core.di.qualifier.concurrent.IO
import ua.frist008.action.record.features.RootSurface
import javax.inject.Inject

@AndroidEntryPoint class SingleActivity : ComponentActivity() {

    @Inject @IO lateinit var ioDispatcher: CoroutineDispatcher

    private val isAdsStarted = atomic(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        gatherAdsConsent()
        setContent { RootSurface() }
    }

    /**
     * Updates the consent to ads on every start, as Google's guide requires, shows its form where
     * the law asks for one (EEA, UK), and starts the ads once they may be requested.
     */
    private fun gatherAdsConsent() {
        val consentInformation = UserMessagingPlatform.getConsentInformation(this)
        val parameters = ConsentRequestParameters.Builder().build()
        val onUpdated = ConsentInformation.OnConsentInfoUpdateSuccessListener {
            UserMessagingPlatform.loadAndShowConsentFormIfRequired(this) { formError ->
                if (formError != null) Timber.w(formError.message)

                initAdsIfAllowed(consentInformation)
            }
        }
        val onFailed = ConsentInformation.OnConsentInfoUpdateFailureListener { requestConsentError ->
            Timber.w(requestConsentError.message)
            initAdsIfAllowed(consentInformation)
        }

        consentInformation.requestConsentInfoUpdate(this, parameters, onUpdated, onFailed)
        initAdsIfAllowed(consentInformation)
    }

    /** Starts the ads once per activity: the consent of an earlier start allows them at once */
    private fun initAdsIfAllowed(consentInformation: ConsentInformation) {
        if (consentInformation.canRequestAds() && isAdsStarted.compareAndSet(false, true)) {
            initAds()
        }
    }

    /**
     * Starts the Mobile Ads SDK off the main thread, as Google's guide requires, with the request configuration passed
     * in, so the first request already carries it.
     */
    private fun initAds() {
        val requestConfiguration = RequestConfiguration.Builder()
            .setMaxAdContentRating(MaxAdContentRating.MAX_AD_CONTENT_RATING_PG)
            .setAgeRestrictedTreatment(AgeRestrictedTreatment.CHILD)
            .build()
        val initializationConfig = InitializationConfig.Builder(getString(R.string.ad_app_id))
            .setRequestConfiguration(requestConfiguration)
            .build()
        lifecycleScope.launch(ioDispatcher) {
            MobileAds.initialize(applicationContext, initializationConfig) {}
        }
    }
}
