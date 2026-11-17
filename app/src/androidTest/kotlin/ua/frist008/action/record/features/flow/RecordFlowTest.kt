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
import ua.frist008.action.record.core.util.date.DateUtils
import ua.frist008.action.record.core.util.io.gb
import ua.frist008.action.record.core.util.io.mb
import ua.frist008.action.record.data.SourceModule
import ua.frist008.action.record.data.network.device.DeviceRadarNetworkSource
import ua.frist008.action.record.data.network.premium.PremiumNetworkSource
import ua.frist008.action.record.data.network.record.RecordNetworkSource
import ua.frist008.action.record.features.AppFlow
import ua.frist008.action.record.features.FakePc
import ua.frist008.action.record.features.FakePlay
import kotlin.time.Duration.Companion.seconds

/**
 * The user records, pauses and stops from the phone and watches what the recorder reports; the
 * network layer to the PC is faked by [FakePc].
 */
@HiltAndroidTest
@UninstallModules(SourceModule::class)
@RunWith(AndroidJUnit4::class)
class RecordFlowTest {

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
     * The user records with the camera and the microphone on:
     * 1. the PC is found and stopped — «Play» shown, no «Stop», no camera or microphone;
     * 2. «Play» — the PC gets byte 1; «Pause» and «Stop», the camera and the microphone are shown;
     * 3. the PC reports 1:05 of recording — the time shows it;
     * 4. the phone is rotated — still recording, the session is not opened again;
     * 5. «Pause» — the PC gets byte 1; «Play» and «Stop», no camera or microphone;
     * 6. «Play» — the PC gets byte 1; recording again;
     * 7. «Stop» — the PC gets byte 3; «Play» alone;
     * 8. the sound icon — «Sounds on» turns into «Sounds off» and back.
     */
    @Test
    fun user_recordsPausesAndStops() {
        val play = appFlow.getString(R.string.record_action_record)
        val pause = appFlow.getString(R.string.record_action_pause)
        val stop = appFlow.getString(R.string.record_action_stop)
        val webCam = appFlow.getString(R.string.record_indicator_webcam)
        val mic = appFlow.getString(R.string.record_indicator_mic)
        val soundOn = appFlow.getString(R.string.record_sound_on)
        val soundOff = appFlow.getString(R.string.record_sound_off)
        val duration = 65.seconds

        fakePc.updateStatus { status -> status.copy(isWebCam = true, isMic = true) }
        fakePc.appear()
        appFlow.launch()

        appFlow.waitFor(FakePc.DEFAULT_STATUS.engine)
        appFlow.waitForDescription(play)
        appFlow.findDescription(stop).assertDoesNotExist()
        appFlow.findDescription(webCam).assertDoesNotExist()
        appFlow.findDescription(mic).assertDoesNotExist()

        appFlow.tapDescription(play)
        appFlow.waitForDescription(pause)
        appFlow.findDescription(stop).assertIsDisplayed()
        appFlow.findDescription(webCam).assertIsDisplayed()
        appFlow.findDescription(mic).assertIsDisplayed()
        assertEquals("«Play» sent", listOf(1), getReceivedBytes())

        fakePc.updateStatus { status -> status.copy(duration = duration) }
        appFlow.waitFor(DateUtils.formatFullTime(duration))

        appFlow.rotate()
        appFlow.waitForDescription(pause)
        appFlow.findDescription(webCam).assertIsDisplayed()
        assertEquals("rotation kept the session", 1, fakePc.connectCount)

        appFlow.tapDescription(pause)
        appFlow.waitForDescription(play)
        appFlow.findDescription(stop).assertIsDisplayed()
        appFlow.waitUntilDescriptionGone(webCam)
        appFlow.findDescription(mic).assertDoesNotExist()
        assertEquals("«Pause» sent", listOf(1, 1), getReceivedBytes())

        appFlow.tapDescription(play)
        appFlow.waitForDescription(pause)
        assertEquals("«Play» after the pause sent", listOf(1, 1, 1), getReceivedBytes())

        appFlow.tapDescription(stop)
        appFlow.waitUntilDescriptionGone(stop)
        appFlow.findDescription(play).assertIsDisplayed()
        assertEquals("«Stop» sent", listOf(1, 1, 1, 3), getReceivedBytes())

        appFlow.tapDescription(soundOn)
        appFlow.waitForDescription(soundOff)
        appFlow.tapDescription(soundOff)
        appFlow.waitForDescription(soundOn)
    }

    /**
     * The user watches what the recorder reports while nothing is recorded:
     * 1. the PC streams — the title «Stream» and the «Live» header;
     * 2. the stream ends and the disk has 9 GB free — the storage shows it;
     * 3. the disk has 500 MB free — the storage shows it in MB;
     * 4. the game closes — «NO ACTIVE GAME FOUND»;
     * 5. the game opens again — the record screen comes back.
     */
    @Test
    fun user_watchesTheStateOfTheRecorder() {
        val engine = FakePc.DEFAULT_STATUS.engine

        fakePc.appear()
        appFlow.launch()
        appFlow.waitFor(engine)

        fakePc.updateStatus { status -> status.copy(isStream = true) }
        appFlow.waitFor(appFlow.getString(R.string.record_title_stream))
        appFlow.findText(appFlow.getString(R.string.record_header_live)).assertIsDisplayed()

        fakePc.updateStatus { status -> status.copy(isStream = false, freeSpace = 9.gb) }
        appFlow.waitFor(appFlow.getString(R.string.record_header_storage_gb_pattern, 9f))
        appFlow.findText(appFlow.getString(R.string.record_title_record)).assertIsDisplayed()

        fakePc.updateStatus { status -> status.copy(freeSpace = 500.mb) }
        appFlow.waitFor(appFlow.getString(R.string.record_header_storage_mb_pattern, 500f))

        fakePc.updateStatus { status -> status.copy(gameActive = false) }
        appFlow.waitFor(appFlow.getString(R.string.record_inactive_subtitle))

        fakePc.updateStatus { status -> status.copy(gameActive = true) }
        appFlow.waitFor(engine)
    }

    private fun getReceivedBytes(): List<Int> =
        fakePc.receivedCommands.map { command -> command.commandInt }
}
