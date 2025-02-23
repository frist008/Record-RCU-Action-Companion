package ua.frist008.action.record.features.device

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ua.frist008.action.record.R
import ua.frist008.action.record.features.device.entity.DevicesProgressState
import kotlin.time.Duration.Companion.seconds

/**
 * Tests for [DevicesProgressScreen] – the screen shown while the app scans for devices
 * or after the scan timer has expired with no results found.
 *
 * These are pure composable tests: no ViewModels, no Hilt.
 * We pass state directly to the composable and assert on what is rendered.
 */
@RunWith(AndroidJUnit4::class)
class DevicesProgressScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    // ──────────────────────────────────────────────
    // Loading state (DevicesProgressState with no duration)
    // ──────────────────────────────────────────────

    /**
     * While the device scanner is actively searching (isLoading = true),
     * the screen shows the "Searching for devices…" headline.
     */
    @Test
    fun showsSearchingText_whenLoading() {
        composeTestRule.setContent {
            DevicesProgressScreen(state = DevicesProgressState())
        }

        val expected = composeTestRule.activity.getString(R.string.device_searching)
        composeTestRule.onNodeWithText(expected).assertIsDisplayed()
    }

    // ──────────────────────────────────────────────
    // Not-found state (DevicesProgressState with a duration)
    // ──────────────────────────────────────────────

    /**
     * After the scan timer elapses without finding any device (isLoading = false),
     * the headline switches to "Device not found".
     */
    @Test
    fun showsNotFoundText_whenSearchTimeout() {
        composeTestRule.setContent {
            DevicesProgressScreen(state = DevicesProgressState(5.seconds))
        }

        val expected = composeTestRule.activity.getString(R.string.device_error_not_found)
        composeTestRule.onNodeWithText(expected).assertIsDisplayed()
    }

    /**
     * The countdown value shown on screen is derived from the passed duration:
     * timerValue = (duration.inWholeSeconds + 1).toString() → 5 s → "6".
     */
    @Test
    fun showsTimerCountdown_whenSearchTimeout() {
        composeTestRule.setContent {
            DevicesProgressScreen(state = DevicesProgressState(5.seconds))
        }

        // DevicesProgressState(5.seconds) → timerValue = "6"
        composeTestRule.onNodeWithText("6").assertIsDisplayed()
    }

    // ──────────────────────────────────────────────
    // Footer button
    // ──────────────────────────────────────────────

    /**
     * The "More info" link button is always visible (both in loading and not-found states)
     * and invokes the provided callback when tapped.
     */
    @Test
    fun invokesCallback_whenHelpLinkClicked() {
        var clicked = false

        composeTestRule.setContent {
            DevicesProgressScreen(
                state = DevicesProgressState(),
                onLinkCLick = { clicked = true },
            )
        }

        val buttonText = composeTestRule.activity.getString(R.string.device_error_more)
        composeTestRule.onNodeWithText(buttonText).performClick()

        assertTrue("Help link callback was not invoked", clicked)
    }
}
