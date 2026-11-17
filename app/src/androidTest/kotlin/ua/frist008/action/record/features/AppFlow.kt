package ua.frist008.action.record.features

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.click
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTouchInput
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.rules.ExternalResource
import ua.frist008.action.record.SingleActivity
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Starts the app through [SingleActivity] once the fakes are bound and walks it as a user does; the
 * data the app keeps on the phone is put back as it was after the flow.
 *
 * @property composeTestRule the empty rule that finds the content of the started activity.
 */
class AppFlow(private val composeTestRule: ComposeTestRule) : ExternalResource() {

    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private var scenario: ActivityScenario<SingleActivity>? = null
    private var dataStore: DataStore<Preferences>? = null
    private var savedPreferences: Preferences? = null

    /** Opens the app as the launcher does, with no PC remembered and the default settings */
    fun launch() {
        val appDataStore = EntryPointAccessors
            .fromApplication(context, AppFlowEntryPoint::class.java)
            .dataStore()

        runBlocking {
            savedPreferences = appDataStore.data.first()
            appDataStore.edit { preferences -> preferences.clear() }
        }

        dataStore = appDataStore
        scenario = ActivityScenario.launch(SingleActivity::class.java)
    }

    fun getString(@StringRes id: Int, vararg formatArgs: Any): String =
        context.getString(id, *formatArgs)

    fun waitFor(text: String, timeout: Duration = DEFAULT_TIMEOUT) {
        composeTestRule.waitUntil(timeout.inWholeMilliseconds) {
            composeTestRule.onAllNodes(hasText(text)).fetchSemanticsNodes().isNotEmpty()
        }
    }

    fun waitForDescription(description: String, timeout: Duration = DEFAULT_TIMEOUT) {
        composeTestRule.waitUntil(timeout.inWholeMilliseconds) {
            composeTestRule.onAllNodes(hasContentDescription(description))
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }

    fun waitUntilGone(text: String, timeout: Duration = DEFAULT_TIMEOUT) {
        composeTestRule.waitUntil(timeout.inWholeMilliseconds) {
            composeTestRule.onAllNodes(hasText(text)).fetchSemanticsNodes().isEmpty()
        }
    }

    fun waitUntilDescriptionGone(description: String, timeout: Duration = DEFAULT_TIMEOUT) {
        composeTestRule.waitUntil(timeout.inWholeMilliseconds) {
            composeTestRule.onAllNodes(hasContentDescription(description))
                .fetchSemanticsNodes()
                .isEmpty()
        }
    }

    fun waitUntil(timeout: Duration = DEFAULT_TIMEOUT, condition: () -> Boolean) {
        composeTestRule.waitUntil(timeout.inWholeMilliseconds, condition)
    }

    fun isShown(text: String): Boolean =
        composeTestRule.onAllNodes(hasText(text)).fetchSemanticsNodes().isNotEmpty()

    fun findText(text: String): SemanticsNodeInteraction = composeTestRule.onNodeWithText(text)

    fun findDescription(description: String): SemanticsNodeInteraction =
        composeTestRule.onNodeWithContentDescription(description)

    fun scrollTo(text: String): SemanticsNodeInteraction = findText(text).performScrollTo()

    fun tap(text: String) {
        findText(text).performClick()
    }

    fun tapDescription(description: String) {
        findDescription(description).performClick()
    }

    /** Taps twice in a row before the screen can change, as a hurried finger does */
    fun doubleTapDescription(description: String) {
        findDescription(description).performTouchInput {
            click()
            click()
        }
    }

    /** Whether the app is still open in front of the user */
    fun isOpen(): Boolean = scenario?.state == Lifecycle.State.RESUMED

    /** Presses the back of the system, as the gesture or the button of the phone does */
    fun pressBack() {
        scenario?.onActivity { activity -> activity.onBackPressedDispatcher.onBackPressed() }
    }

    /** Turns the phone, so the activity is made again with the app running */
    fun rotate() {
        scenario?.recreate()
    }

    /** Leaves the app for the home screen and opens it again from the recents */
    fun leaveAndReturn() {
        scenario?.moveToState(Lifecycle.State.CREATED)
        scenario?.moveToState(Lifecycle.State.RESUMED)
    }

    override fun after() {
        scenario?.close()

        val appDataStore = dataStore ?: return
        val preferences = savedPreferences ?: return

        runBlocking {
            appDataStore.edit { mutablePreferences ->
                mutablePreferences.clear()
                mutablePreferences += preferences
            }
        }
    }

    /** The graph of the app, read by the flow before the app starts */
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface AppFlowEntryPoint {

        fun dataStore(): DataStore<Preferences>
    }

    companion object {

        private val DEFAULT_TIMEOUT = 15.seconds
    }
}
