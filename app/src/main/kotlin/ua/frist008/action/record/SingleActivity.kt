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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import ua.frist008.action.record.core.di.qualifier.concurrent.IO
import ua.frist008.action.record.features.RootSurface
import javax.inject.Inject

@AndroidEntryPoint class SingleActivity : ComponentActivity() {

    @Inject @IO lateinit var ioDispatcher: CoroutineDispatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        initAds()
        setContent { RootSurface() }
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
