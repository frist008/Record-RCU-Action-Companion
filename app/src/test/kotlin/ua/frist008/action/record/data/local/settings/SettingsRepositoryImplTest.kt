package ua.frist008.action.record.data.local.settings

import android.content.Context
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ua.frist008.action.record.core.util.io.gb
import ua.frist008.action.record.features.settings.entity.SettingsDomainEntity
import kotlin.time.Duration.Companion.minutes

class SettingsRepositoryImplTest {

    private val settingsDAO: SettingsDAO = mockk()
    private val context: Context = mockk()
    private lateinit var repository: SettingsRepositoryImpl

    private val savedSettings = SettingsDomainEntity(
        minFreeSpace = 20.gb,
        isDurationSound = true,
        durationSoundAfter = 45.minutes,
        dimToPercent = 30,
    )

    @Before
    fun setUp() {
        repository = SettingsRepositoryImpl(settingsDAO, context)
    }

    @Test
    fun `settingsFlow emits the settings of DAO`() = runTest {
        every { settingsDAO.settingsFlow } returns flowOf(savedSettings)

        val result = repository.settingsFlow.first()

        assertEquals(savedSettings, result)
    }

    @Test
    fun `update passes the change to DAO`() = runTest {
        val transformSlot = slot<(SettingsDomainEntity) -> SettingsDomainEntity>()

        coJustRun { settingsDAO.update(capture(transformSlot)) }

        repository.update { settings -> settings.copy(isFirstTouchOnlyWakes = true) }

        val expected = savedSettings.copy(isFirstTouchOnlyWakes = true)

        coVerify(exactly = 1) { settingsDAO.update(any()) }
        assertEquals(expected, transformSlot.captured(savedSettings))
    }

    @Test
    fun `update keeps every other setting`() = runTest {
        val transformSlot = slot<(SettingsDomainEntity) -> SettingsDomainEntity>()

        coJustRun { settingsDAO.update(capture(transformSlot)) }

        repository.update { settings -> settings.copy(dimToPercent = 50) }

        val result = transformSlot.captured(savedSettings)

        assertEquals(20.gb, result.minFreeSpace)
        assertEquals(45.minutes, result.durationSoundAfter)
        assertEquals(50, result.dimToPercent)
    }
}