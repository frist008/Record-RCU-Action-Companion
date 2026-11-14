package ua.frist008.action.record.features.connection

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ua.frist008.action.record.R
import ua.frist008.action.record.features.connection.entity.ConnectionProgressState
import kotlin.time.Duration.Companion.seconds

/**
 * Tests for [ConnectionProgressScreen] – the screen shown while the app scans for the PC
 * or after the scan timer has expired with no results found.
 *
 * These are pure composable tests: no ViewModels, no Hilt.
 * We pass state directly to the composable and assert on what is rendered.
 */
@RunWith(AndroidJUnit4::class)
class ConnectionProgressScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    // ──────────────────────────────────────────────
    // Loading state (ConnectionProgressState with no duration)
    // ──────────────────────────────────────────────

    /**
     * While the scanner is actively searching (isLoading = true),
     * the screen shows the "Searching for devices…" headline.
     */
    @Test
    fun showsSearchingText_whenLoading() {
        composeTestRule.setContent {
            ConnectionProgressScreen(state = ConnectionProgressState())
        }

        val expected = composeTestRule.activity.getString(R.string.device_searching)
        composeTestRule.onNodeWithText(expected).assertIsDisplayed()
    }

    // ──────────────────────────────────────────────
    // Not-found state (ConnectionProgressState with a duration)
    // ──────────────────────────────────────────────

    /**
     * After the scan timer elapses without finding any PC (isLoading = false),
     * the headline switches to "Device not found".
     */
    @Test
    fun showsNotFoundText_whenSearchTimeout() {
        composeTestRule.setContent {
            ConnectionProgressScreen(state = ConnectionProgressState(5.seconds))
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
            ConnectionProgressScreen(state = ConnectionProgressState(5.seconds))
        }

        // ConnectionProgressState(5.seconds) → timerValue = "6"
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
        var isClicked = false

        composeTestRule.setContent {
            ConnectionProgressScreen(
                state = ConnectionProgressState(),
                onLinkCLick = { isClicked = true },
            )
        }

        val buttonText = composeTestRule.activity.getString(R.string.device_error_more)

        composeTestRule.onNodeWithText(buttonText).performClick()

        assertTrue("Help link callback was not invoked", isClicked)
    }
}