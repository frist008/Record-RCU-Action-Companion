package ua.frist008.action.record.features.flow

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createEmptyComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ua.frist008.action.record.R
import ua.frist008.action.record.data.SourceModule
import ua.frist008.action.record.data.network.device.DeviceRadarNetworkSource
import ua.frist008.action.record.data.network.premium.PremiumNetworkSource
import ua.frist008.action.record.data.network.record.RecordNetworkSource
import ua.frist008.action.record.features.AppFlow
import ua.frist008.action.record.features.FakePc
import ua.frist008.action.record.features.FakePlay
import kotlin.time.Duration.Companion.seconds

/**
 * The user opens the app before the PC is on the network, and the app finds the PC, connects to it,
 * loses it and finds it again on its own; the network layer to the PC is faked by [FakePc].
 */
@HiltAndroidTest
@UninstallModules(SourceModule::class)
@RunWith(AndroidJUnit4::class)
class ConnectionFlowTest {

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
     * The user waits for the PC and the app follows it:
     * 1. no PC on the network — the search and «Devices not found…» with its countdown take turns,
     *    «How to fix?» stays on the screen;
     * 2. the PC appears — the app opens the record screen by itself, «CONNECTION» first;
     * 3. the PC reports its state — the record screen shows its 3D engine and storage;
     * 4. the phone is rotated — the screen stays, the session is not opened again;
     * 5. the app goes to the background and comes back — the session stays;
     * 6. the PC leaves the network — «CONNECTION», then the search within 10 s;
     * 7. the PC comes back — the app connects again and shows the record screen.
     */
    @Test
    fun user_waitsForThePcAndTheAppFollowsIt() {
        val searching = appFlow.getString(R.string.device_searching)
        val notFound = appFlow.getString(R.string.device_error_not_found)
        val howToFix = appFlow.getString(R.string.device_error_more)
        val connecting = appFlow.getString(R.string.record_connection)
        val engine = FakePc.DEFAULT_STATUS.engine
        val storage = appFlow.getString(R.string.record_header_storage_gb_pattern, 30f)

        appFlow.launch()

        appFlow.waitFor(notFound)
        appFlow.findText(howToFix).assertIsDisplayed()
        appFlow.waitFor(searching)
        appFlow.findText(howToFix).assertIsDisplayed()

        fakePc.appear()
        appFlow.waitFor(engine)
        appFlow.findText(storage).assertIsDisplayed()
        appFlow.findText(appFlow.getString(R.string.record_title_record)).assertIsDisplayed()
        assertEquals("one session with the PC", 1, fakePc.connectCount)

        appFlow.rotate()
        appFlow.waitFor(engine)
        assertEquals("rotation kept the session", 1, fakePc.connectCount)

        appFlow.leaveAndReturn()
        appFlow.waitFor(engine)
        assertEquals("the background kept the session", 1, fakePc.connectCount)

        fakePc.disappear()
        appFlow.waitFor(connecting)
        appFlow.waitUntil(timeout = 20.seconds) {
            appFlow.isShown(searching) || appFlow.isShown(notFound)
        }

        fakePc.appear()
        appFlow.waitFor(engine)
        assertEquals("a new session after the PC came back", 2, fakePc.connectCount)
    }
}
