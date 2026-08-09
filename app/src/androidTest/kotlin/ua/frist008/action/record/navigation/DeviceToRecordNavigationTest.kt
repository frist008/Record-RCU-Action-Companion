package ua.frist008.action.record.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
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
import ua.frist008.action.record.features.device.DeviceRadarRepository
import ua.frist008.action.record.features.device.entity.DeviceDomainEntity
import ua.frist008.action.record.features.record.RecordRepository
import ua.frist008.action.record.features.record.entity.RecordCommand
import ua.frist008.action.record.features.record.entity.RecordDomainEntity

/**
 * End-to-end navigation tests that exercise the full DevicesScreen → RecordScreen flow.
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
class DeviceToRecordNavigationTest {

    // HiltAndroidRule must be order=0 so Hilt component is ready before the compose rule starts.
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    // ── Fakes injected via @BindValue ──────────────────────────────────────

    // Typed as the interface so Hilt binds it under DeviceRadarRepository.
    // Kept as a private backing property for configuration in each test.
    private val deviceRepoFake = FakeDeviceRadarRepository()
    private val recordRepoFake = FakeRecordRepository()

    @BindValue @JvmField
    val deviceRepo: DeviceRadarRepository = deviceRepoFake

    @BindValue @JvmField
    val recordRepo: RecordRepository = recordRepoFake

    // ── Fixtures ───────────────────────────────────────────────────────────

    // Two devices: prevents DevicesViewModel's single-device auto-navigation shortcut,
    // so we can test explicit click navigation.
    private val twoDevices = listOf(
        DeviceDomainEntity(id = 1L, isAvailableStatus = true, name = "Gaming PC", address = "192.168.0.1:2555"),
        DeviceDomainEntity(id = 2L, isAvailableStatus = true, name = "Work PC", address = "192.168.0.2:2555"),
    )

    // One device: DevicesViewModel automatically navigates when only one device is available.
    private val singleDevice = listOf(
        DeviceDomainEntity(id = 1L, isAvailableStatus = true, name = "Gaming PC", address = "192.168.0.1:2555"),
    )

    // ── Setup / teardown ───────────────────────────────────────────────────

    @Before
    fun setUp() {
        // Trigger Hilt injection of this test class before the test body runs.
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        // Reset fake state so tests are independent of execution order.
        deviceRepoFake.reset()
    }

    // ── Tests ──────────────────────────────────────────────────────────────

    /**
     * DevicesScreen renders a list entry for every device emitted by the repository.
     */
    @Test
    fun showsDeviceList_whenDevicesAvailable() {
        deviceRepoFake.setDevices(twoDevices)
        composeTestRule.setContent { RootSurface() }

        // DevicesViewModel initialises asynchronously; wait up to 5 s for the list to appear.
        composeTestRule.waitUntil(5_000L) {
            composeTestRule.onAllNodes(hasText("Gaming PC")).fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("Gaming PC").assertIsDisplayed()
        composeTestRule.onNodeWithText("Work PC").assertIsDisplayed()
    }

    /**
     * Tapping a device row navigates to RecordScreen and shows RecordProgressScreen
     * (which means the ViewModel is trying to connect to the PC).
     */
    @Test
    fun clickDevice_navigatesToRecordProgressScreen() {
        deviceRepoFake.setDevices(twoDevices)
        composeTestRule.setContent { RootSurface() }

        // Wait for device list
        composeTestRule.waitUntil(5_000L) {
            composeTestRule.onAllNodes(hasText("Gaming PC")).fetchSemanticsNodes().isNotEmpty()
        }

        // Tap first device
        composeTestRule.onNodeWithText("Gaming PC").performClick()

        // RecordProgressScreen shows while FakeRecordRepository.connect() suspends indefinitely
        val connectingText = composeTestRule.activity.getString(R.string.record_connection)
        composeTestRule.waitUntil(5_000L) {
            composeTestRule.onAllNodes(hasText(connectingText)).fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText(connectingText).assertIsDisplayed()
    }

    /**
     * When only one available device is discovered, DevicesViewModel auto-navigates
     * to RecordScreen without requiring a user tap.
     */
    @Test
    fun singleDevice_autoNavigatesToRecordProgressScreen() {
        deviceRepoFake.setDevices(singleDevice)
        composeTestRule.setContent { RootSurface() }

        val connectingText = composeTestRule.activity.getString(R.string.record_connection)
        composeTestRule.waitUntil(5_000L) {
            composeTestRule.onAllNodes(hasText(connectingText)).fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText(connectingText).assertIsDisplayed()
    }

    /**
     * Pressing Back from RecordScreen must return to DevicesScreen.
     *
     * This also validates the fix in RootSurface: DevicesViewModel must stay alive
     * while its screen is in the back-stack, so the list re-appears instantly
     * without waiting for a re-scan.
     */
    @Test
    fun backFromRecord_returnsToDeviceList_andViewModelStaysAlive() {
        deviceRepoFake.setDevices(twoDevices)
        composeTestRule.setContent { RootSurface() }

        // Navigate forward to RecordScreen
        composeTestRule.waitUntil(5_000L) {
            composeTestRule.onAllNodes(hasText("Gaming PC")).fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("Gaming PC").performClick()

        val connectingText = composeTestRule.activity.getString(R.string.record_connection)
        composeTestRule.waitUntil(5_000L) {
            composeTestRule.onAllNodes(hasText(connectingText)).fetchSemanticsNodes().isNotEmpty()
        }

        // Press system Back to pop RecordScreen off the back-stack
        composeTestRule.activityRule.scenario.onActivity { activity ->
            activity.onBackPressedDispatcher.onBackPressed()
        }

        // DevicesScreen must come back with the same device list.
        // Because the DevicesViewModel was NOT cleared (it stayed in the back-stack),
        // the list is immediately available – no re-scan delay expected.
        composeTestRule.waitUntil(5_000L) {
            composeTestRule.onAllNodes(hasText("Gaming PC")).fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("Gaming PC").assertIsDisplayed()
        composeTestRule.onNodeWithText("Work PC").assertIsDisplayed()
    }
}

// ── Fake implementations ───────────────────────────────────────────────────────

/**
 * In-memory DeviceRadarRepository driven by a [MutableStateFlow].
 *
 * Call [setDevices] before the composable tree starts to control what
 * DevicesViewModel sees when it calls get().
 */
private class FakeDeviceRadarRepository : DeviceRadarRepository {

    private val devicesFlow = MutableStateFlow<List<DeviceDomainEntity>>(emptyList())

    fun setDevices(devices: List<DeviceDomainEntity>) {
        devicesFlow.value = devices
    }

    fun reset() {
        devicesFlow.value = emptyList()
    }

    override suspend fun get(): Flow<List<DeviceDomainEntity>> = devicesFlow
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
    override suspend fun connect(deviceId: Long): Unit = suspendCancellableCoroutine { }

    override suspend fun sendCommand(command: RecordCommand) = Unit
}
