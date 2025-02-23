package ua.frist008.action.record.features.record

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ua.frist008.action.record.R

/**
 * Tests for [RecordProgressScreen] – the screen shown while the app is establishing
 * a connection to the selected PC.
 *
 * Pure composable test: no state parameters, no ViewModels, no Hilt.
 */
@RunWith(AndroidJUnit4::class)
class RecordProgressScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    /**
     * The "Connecting to PC…" text must be visible as soon as the screen is displayed.
     * This is the primary signal to the user that a connection attempt is in progress.
     */
    @Test
    fun showsConnectingText() {
        composeTestRule.setContent {
            RecordProgressScreen()
        }

        val expected = composeTestRule.activity.getString(R.string.record_connection)
        composeTestRule.onNodeWithText(expected).assertIsDisplayed()
    }
}
