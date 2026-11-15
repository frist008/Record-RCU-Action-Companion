package ua.frist008.action.record.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ua.frist008.action.record.HiltTestActivity
import ua.frist008.action.record.R
import ua.frist008.action.record.data.RepositoryModule
import ua.frist008.action.record.features.RootSurface
import ua.frist008.action.record.features.connection.ConnectionRepository
import ua.frist008.action.record.features.connection.entity.DeviceDomainEntity
import ua.frist008.action.record.features.record.RecordRepository
import ua.frist008.action.record.features.record.entity.RecordCommand
import ua.frist008.action.record.features.record.entity.RecordDomainEntity
import ua.frist008.action.record.features.settings.SettingsRepository
import ua.frist008.action.record.features.settings.entity.SettingsDomainEntity

/**
 * End-to-end navigation tests that exercise the full ConnectionScreen → RecordScreen flow.
 *
 * We use @HiltAndroidTest so the real Hilt component is built, with only the two
 * network repositories replaced by in-memory fakes. Everything else (Navigator,
 * PresentationDependencies, Vibrator, …) is the real production implementation.
 *
 * @BindValue injects the fake instances into the Hilt component.
 * @UninstallModules removes the real RepositoryModule that would otherwise clash.
 */
@HiltAndroidTest
@UninstallModules(RepositoryModule::class)
@RunWith(AndroidJUnit4::class)
class ConnectionToRecordNavigationTest {

    // HiltAndroidRule must be order=0 so Hilt component is ready before the compose rule starts.
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    // ── Fakes injected via @BindValue ──────────────────────────────────────

    private val connectionRepoFake = FakeConnectionRepository()
    private val recordRepoFake = FakeRecordRepository()

    @BindValue @JvmField
    val connectionRepo: ConnectionRepository = connectionRepoFake

    @BindValue @JvmField
    val recordRepo: RecordRepository = recordRepoFake

    @BindValue @JvmField
    val settingsRepository: SettingsRepository = FakeSettingsRepository()

    // ── Fixtures ───────────────────────────────────────────────────────────

    private val device = DeviceDomainEntity(name = "Gaming PC", address = "192.168.0.1:2555")

    // ── Setup / teardown ───────────────────────────────────────────────────

    @Before
    fun setUp() {
        // Trigger Hilt injection of this test class before the test body runs.
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        // Reset fake state so tests are independent of execution order.
        connectionRepoFake.setDevice(null)
    }

    // ── Tests ──────────────────────────────────────────────────────────────

    /**
     * While no PC replied, ConnectionScreen shows the search.
     */
    @Test
    fun noDevice_showsSearchingText() {
        composeTestRule.setContent { RootSurface() }

        val searchingText = composeTestRule.activity.getString(R.string.device_searching)

        composeTestRule.waitUntil(5_000L) {
            composeTestRule.onAllNodes(hasText(searchingText)).fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText(searchingText).assertIsDisplayed()
    }

    /**
     * When a PC replies, ConnectionViewModel opens RecordScreen without a tap, and
     * RecordProgressScreen shows while FakeRecordRepository.connect() suspends indefinitely.
     */
    @Test
    fun device_autoNavigatesToRecordProgressScreen() {
        connectionRepoFake.setDevice(device)
        composeTestRule.setContent { RootSurface() }

        val connectingText = composeTestRule.activity.getString(R.string.record_connection)

        composeTestRule.waitUntil(5_000L) {
            composeTestRule.onAllNodes(hasText(connectingText)).fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText(connectingText).assertIsDisplayed()
    }

    /**
     * A PC that replies after the search started still opens RecordScreen.
     */
    @Test
    fun deviceFoundLater_navigatesToRecordProgressScreen() {
        composeTestRule.setContent { RootSurface() }

        val searchingText = composeTestRule.activity.getString(R.string.device_searching)

        composeTestRule.waitUntil(5_000L) {
            composeTestRule.onAllNodes(hasText(searchingText)).fetchSemanticsNodes().isNotEmpty()
        }

        connectionRepoFake.setDevice(device)

        val connectingText = composeTestRule.activity.getString(R.string.record_connection)

        composeTestRule.waitUntil(5_000L) {
            composeTestRule.onAllNodes(hasText(connectingText)).fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText(connectingText).assertIsDisplayed()
    }
}

// ── Fake implementations ───────────────────────────────────────────────────────

/**
 * In-memory ConnectionRepository driven by a [MutableStateFlow].
 *
 * Call [setDevice] to control what ConnectionViewModel sees when it calls get().
 */
private class FakeConnectionRepository : ConnectionRepository {

    private val deviceFlow = MutableStateFlow<DeviceDomainEntity?>(null)

    fun setDevice(device: DeviceDomainEntity?) {
        deviceFlow.value = device
    }

    override fun get(): Flow<DeviceDomainEntity?> = deviceFlow
}

/**
 * In-memory RecordRepository that suspends [connect] forever, keeping
 * RecordViewModel in UIState.Progress (→ RecordProgressScreen stays visible).
 *
 * [recordFlow] never emits, so no success/error state is reached.
 */
private class FakeRecordRepository : RecordRepository {

    override val recordFlow: Flow<RecordDomainEntity> = MutableSharedFlow()

    // Suspend forever; the coroutine is cancelled when RecordViewModel is cleared.
    override suspend fun connect(): Unit = suspendCancellableCoroutine { }

    override suspend fun sendCommand(command: RecordCommand) = Unit
}

/** In-memory SettingsRepository that keeps the settings of one test run */
private class FakeSettingsRepository : SettingsRepository {

    private val settingsMutableFlow = MutableStateFlow(SettingsDomainEntity())

    override val settingsFlow: Flow<SettingsDomainEntity> = settingsMutableFlow

    override suspend fun update(transform: (SettingsDomainEntity) -> SettingsDomainEntity) {
        settingsMutableFlow.value = transform(settingsMutableFlow.value)
    }

    override fun getAppLanguage(): String? = null
}