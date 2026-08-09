package ua.frist008.action.record.features.device

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.collections.immutable.persistentListOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ua.frist008.action.record.R
import ua.frist008.action.record.features.device.entity.DeviceSuccessState
import ua.frist008.action.record.features.device.entity.DevicesSuccessState

/**
 * Tests for [DevicesSuccessScreen] – the list of discovered devices.
 *
 * Pure composable tests: state is injected directly, no ViewModels or Hilt.
 */
@RunWith(AndroidJUnit4::class)
class DevicesSuccessScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    // Helpers ─────────────────────────────────────

    private val onlineDevice = DeviceSuccessState(
        id = 1L,
        isAvailableStatus = true,
        name = "Gaming PC",
        address = "192.168.0.1:2555",
    )

    private val offlineDevice = DeviceSuccessState(
        id = 2L,
        isAvailableStatus = false,
        name = "Offline PC",
        address = "192.168.0.2:2555",
    )

    // ──────────────────────────────────────────────
    // Screen structure
    // ──────────────────────────────────────────────

    /**
     * The toolbar must display the screen title defined in strings.xml.
     */
    @Test
    fun showsTitle() {
        composeTestRule.setContent {
            DevicesSuccessScreen(state = DevicesSuccessState(persistentListOf(onlineDevice)))
        }

        val title = composeTestRule.activity.getString(R.string.devices_title)
        composeTestRule.onNodeWithText(title).assertIsDisplayed()
    }

    // ──────────────────────────────────────────────
    // Device list items
    // ──────────────────────────────────────────────

    /**
     * Every device name from the state list must appear in the rendered list.
     */
    @Test
    fun showsDeviceNames() {
        composeTestRule.setContent {
            DevicesSuccessScreen(
                state = DevicesSuccessState(persistentListOf(onlineDevice, offlineDevice)),
            )
        }

        composeTestRule.onNodeWithText("Gaming PC").assertIsDisplayed()
        composeTestRule.onNodeWithText("Offline PC").assertIsDisplayed()
    }

    /**
     * Every device address (IP:port) must appear below the device name.
     */
    @Test
    fun showsDeviceAddresses() {
        composeTestRule.setContent {
            DevicesSuccessScreen(
                state = DevicesSuccessState(persistentListOf(onlineDevice, offlineDevice)),
            )
        }

        composeTestRule.onNodeWithText("192.168.0.1:2555").assertIsDisplayed()
        composeTestRule.onNodeWithText("192.168.0.2:2555").assertIsDisplayed()
    }

    // ──────────────────────────────────────────────
    // Click interactions
    // ──────────────────────────────────────────────

    /**
     * Tapping a device row must invoke the onItemClick callback
     * and pass exactly the tapped device object.
     */
    @Test
    fun clickDevice_invokesCallbackWithCorrectDevice() {
        var clickedDevice: DeviceSuccessState? = null

        composeTestRule.setContent {
            DevicesSuccessScreen(
                state = DevicesSuccessState(persistentListOf(onlineDevice, offlineDevice)),
                onItemClick = { clickedDevice = it },
            )
        }

        composeTestRule.onNodeWithText("Gaming PC").performClick()

        assertEquals("Callback received wrong device", onlineDevice, clickedDevice)
    }

    /**
     * Offline devices must also be tappable so the user can still attempt to connect.
     */
    @Test
    fun offlineDevice_isClickable() {
        var clicked = false

        composeTestRule.setContent {
            DevicesSuccessScreen(
                state = DevicesSuccessState(persistentListOf(offlineDevice)),
                onItemClick = { clicked = true },
            )
        }

        composeTestRule.onNodeWithText("Offline PC").performClick()

        assertTrue("Offline device click was not triggered", clicked)
    }

    /**
     * Tapping a second device in the list must pass that specific device,
     * not the first one.
     */
    @Test
    fun clickSecondDevice_invokesCallbackWithSecondDevice() {
        var clickedDevice: DeviceSuccessState? = null

        composeTestRule.setContent {
            DevicesSuccessScreen(
                state = DevicesSuccessState(persistentListOf(onlineDevice, offlineDevice)),
                onItemClick = { clickedDevice = it },
            )
        }

        composeTestRule.onNodeWithText("Offline PC").performClick()

        assertEquals("Callback received wrong device", offlineDevice, clickedDevice)
    }
}
