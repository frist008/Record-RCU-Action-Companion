package ua.frist008.action.record.features.flow

import android.os.Build
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.v2.createEmptyComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ua.frist008.action.record.BuildConfig
import ua.frist008.action.record.R
import ua.frist008.action.record.data.SourceModule
import ua.frist008.action.record.data.network.device.DeviceRadarNetworkSource
import ua.frist008.action.record.data.network.premium.PremiumNetworkSource
import ua.frist008.action.record.data.network.record.RecordNetworkSource
import ua.frist008.action.record.features.AppFlow
import ua.frist008.action.record.features.FakePc
import ua.frist008.action.record.features.FakePlay

/**
 * The user opens the settings from the record screen, changes them and finds them kept; the
 * network layer to the PC is faked by [FakePc], and the settings of the phone are put back after
 * the flow.
 */
@HiltAndroidTest
@UninstallModules(SourceModule::class)
@RunWith(AndroidJUnit4::class)
class SettingsFlowTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createEmptyComposeRule()

    @get:Rule(order = 2)
    val appFlow = AppFlow(composeTestRule)

    private val fakePc = FakePc()

    @BindValue @JvmField
    val radarSource: DeviceRadarNetworkSource = fakePc

    @BindValue @JvmField
    val recordSource: RecordNetworkSource = fakePc

    @BindValue @JvmField
    val premiumSource: PremiumNetworkSource = FakePlay()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    /**
     * The user goes through every setting:
     * 1. the gear of the record screen opens «Settings»;
     * 2. every setting is shown with its default — 1 GB, «Low space» on, the button sounds and
     *    «Recording time» off with «Signal after» 30 min greyed out, 10 min, 10 %, «First touch»
     *    off, «App language» on Android 13 and newer, «About» with the version;
     * 3. «+» of the minimum free space shows 2 GB; «−» twice stops at 1 GB;
     * 4. «Recording time» on — «Signal after» is enabled, and its «+» shows 35 min;
     * 5. «+» of «Dim to» shows 20 %; «First touch» on;
     * 6. the phone is rotated — the values stay;
     * 7. «About» shows the name and the version of the app and the links «Privacy policy» and
     *    «Open source licenses»; back returns to «Settings»;
     * 8. back returns to the record screen; the gear again shows the values kept;
     * 9. the app goes to the background and comes back — the values stay;
     * 10. the back arrow tapped twice in a row returns to the record screen once, and the app
     *     stays open.
     */
    @Test
    fun user_changesEverySetting() {
        val settings = appFlow.getString(R.string.settings_title)
        val minFreeSpace = appFlow.getString(R.string.settings_min_free_space)
        val lowSpace = appFlow.getString(R.string.settings_low_space)
        val recordButton = appFlow.getString(R.string.settings_record_button)
        val pauseButton = appFlow.getString(R.string.settings_pause_button)
        val stopButton = appFlow.getString(R.string.settings_stop_button)
        val duration = appFlow.getString(R.string.settings_duration)
        val durationAfter = appFlow.getString(R.string.settings_duration_after)
        val dimAfter = appFlow.getString(R.string.settings_dim_after)
        val dimTo = appFlow.getString(R.string.settings_dim_to)
        val firstTouch = appFlow.getString(R.string.settings_first_touch)
        val about = appFlow.getString(R.string.settings_about)
        val version = appFlow.getString(R.string.settings_version_pattern, BuildConfig.VERSION_NAME)
        val increaseDurationAfter =
            appFlow.getString(R.string.settings_increase_pattern, durationAfter)
        val durationAfterChanged = appFlow.getString(R.string.settings_minutes_pattern, 35)
        val dimToChanged = appFlow.getString(R.string.settings_percent_pattern, 20)

        fakePc.appear()
        appFlow.launch()
        appFlow.waitFor(FakePc.DEFAULT_STATUS.engine)

        appFlow.tapDescription(settings)
        appFlow.waitFor(minFreeSpace)

        appFlow.findText(appFlow.getString(R.string.settings_gb_pattern, 1)).assertIsDisplayed()
        appFlow.scrollTo(lowSpace).assertIsOn()
        appFlow.scrollTo(recordButton).assertIsOff()
        appFlow.scrollTo(pauseButton).assertIsOff()
        appFlow.scrollTo(stopButton).assertIsOff()
        appFlow.scrollTo(duration).assertIsOff()
        appFlow.scrollTo(durationAfter).assertIsDisplayed()
        appFlow.findText(appFlow.getString(R.string.settings_minutes_pattern, 30))
            .assertIsDisplayed()
        appFlow.findDescription(increaseDurationAfter).assertIsNotEnabled()
        appFlow.scrollTo(dimAfter).assertIsDisplayed()
        appFlow.findText(appFlow.getString(R.string.settings_minutes_pattern, 10))
            .assertIsDisplayed()
        appFlow.scrollTo(dimTo).assertIsDisplayed()
        appFlow.findText(appFlow.getString(R.string.settings_percent_pattern, 10))
            .assertIsDisplayed()
        appFlow.scrollTo(firstTouch).assertIsOff()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            appFlow.scrollTo(appFlow.getString(R.string.settings_language)).assertIsDisplayed()
        }

        appFlow.scrollTo(about).assertIsDisplayed()
        appFlow.findText(version).assertIsDisplayed()

        changeMinFreeSpace(minFreeSpace)
        turnOnDurationSound(duration, increaseDurationAfter, durationAfterChanged)

        appFlow.scrollTo(dimTo)
        appFlow.tapDescription(appFlow.getString(R.string.settings_increase_pattern, dimTo))
        appFlow.waitFor(dimToChanged)
        appFlow.scrollTo(firstTouch)
        appFlow.tap(firstTouch)
        appFlow.findText(firstTouch).assertIsOn()

        appFlow.rotate()
        appFlow.waitFor(minFreeSpace)
        appFlow.scrollTo(durationAfterChanged).assertIsDisplayed()
        appFlow.scrollTo(dimToChanged).assertIsDisplayed()
        appFlow.scrollTo(firstTouch).assertIsOn()

        openAboutAndReturn(about, version)
        appFlow.waitFor(minFreeSpace)

        appFlow.pressBack()
        appFlow.waitFor(FakePc.DEFAULT_STATUS.engine)
        appFlow.tapDescription(settings)
        appFlow.waitFor(minFreeSpace)
        appFlow.scrollTo(durationAfterChanged).assertIsDisplayed()
        appFlow.scrollTo(duration).assertIsOn()

        appFlow.leaveAndReturn()
        appFlow.waitFor(minFreeSpace)
        appFlow.scrollTo(dimToChanged).assertIsDisplayed()
        appFlow.scrollTo(firstTouch).assertIsOn()

        appFlow.doubleTapDescription(appFlow.getString(R.string.app_name))
        appFlow.waitFor(FakePc.DEFAULT_STATUS.engine)
        assertTrue("The app closed on a double tap of the back arrow", appFlow.isOpen())
    }

    private fun changeMinFreeSpace(minFreeSpace: String) {
        val increase = appFlow.getString(R.string.settings_increase_pattern, minFreeSpace)
        val decrease = appFlow.getString(R.string.settings_decrease_pattern, minFreeSpace)
        val oneGb = appFlow.getString(R.string.settings_gb_pattern, 1)

        appFlow.scrollTo(minFreeSpace)
        appFlow.tapDescription(increase)
        appFlow.waitFor(appFlow.getString(R.string.settings_gb_pattern, 2))
        appFlow.tapDescription(decrease)
        appFlow.waitFor(oneGb)
        appFlow.tapDescription(decrease)
        appFlow.waitFor(oneGb)
    }

    private fun turnOnDurationSound(
        duration: String,
        increaseDurationAfter: String,
        durationAfterChanged: String,
    ) {
        appFlow.scrollTo(duration)
        appFlow.tap(duration)
        appFlow.findText(duration).assertIsOn()
        appFlow.findDescription(increaseDurationAfter).assertIsEnabled()
        appFlow.tapDescription(increaseDurationAfter)
        appFlow.waitFor(durationAfterChanged)
    }

    private fun openAboutAndReturn(about: String, version: String) {
        appFlow.scrollTo(about)
        appFlow.tap(about)
        appFlow.waitFor(appFlow.getString(R.string.settings_about_app_name))
        appFlow.findText(version).assertIsDisplayed()
        appFlow.scrollTo(appFlow.getString(R.string.settings_about_privacy_policy))
            .assertIsDisplayed()
            .assertHasClickAction()
        appFlow.scrollTo(appFlow.getString(R.string.settings_about_licenses))
            .assertIsDisplayed()
            .assertHasClickAction()
        appFlow.pressBack()
    }
}
