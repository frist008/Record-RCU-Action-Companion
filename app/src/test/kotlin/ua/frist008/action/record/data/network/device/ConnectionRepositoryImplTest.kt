package ua.frist008.action.record.data.network.device

import android.content.Context
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import ua.frist008.action.record.data.local.device.DeviceDAO
import ua.frist008.action.record.data.network.device.entity.DeviceDTO

class ConnectionRepositoryImplTest {

    private val deviceDAO: DeviceDAO = mockk()
    private val networkSource: DeviceRadarNetworkSource = mockk()
    private val context: Context = mockk()
    private lateinit var repository: ConnectionRepositoryImpl

    private val firstDto = DeviceDTO(name = "First PC", ip = 16909060L, port = 2555)
    private val secondDto = DeviceDTO(name = "Second PC", ip = 16909061L, port = 2555)

    @Before
    fun setup() {
        repository = ConnectionRepositoryImpl(deviceDAO, networkSource, context)
    }

    @Test
    fun `get emits null when no PC replied`() = runTest {
        every { networkSource.get() } returns flowOf(emptyList())
        coEvery { deviceDAO.get() } returns null

        val result = repository.get().first()

        assertNull(result)
    }

    @Test
    fun `get does not remember anything when no PC replied`() = runTest {
        every { networkSource.get() } returns flowOf(emptyList())
        coEvery { deviceDAO.get() } returns secondDto.toDBO()

        repository.get().first()

        coVerify(exactly = 0) { deviceDAO.insert(any()) }
    }

    @Test
    fun `get picks the last PC when it replied among others`() = runTest {
        every { networkSource.get() } returns flowOf(listOf(firstDto, secondDto))
        coEvery { deviceDAO.get() } returns secondDto.toDBO()

        val result = repository.get().first()

        assertEquals(secondDto.toDomain(), result)
    }

    @Test
    fun `get does not remember the last PC again`() = runTest {
        every { networkSource.get() } returns flowOf(listOf(firstDto, secondDto))
        coEvery { deviceDAO.get() } returns secondDto.toDBO()

        repository.get().first()

        coVerify(exactly = 0) { deviceDAO.insert(any()) }
    }

    @Test
    fun `get picks the first PC when the last one did not reply`() = runTest {
        val lastDto = DeviceDTO(name = "Last PC", ip = 16909062L, port = 2555)

        every { networkSource.get() } returns flowOf(listOf(firstDto, secondDto))
        coEvery { deviceDAO.get() } returns lastDto.toDBO()
        coJustRun { deviceDAO.insert(any()) }

        val result = repository.get().first()

        assertEquals(firstDto.toDomain(), result)
    }

    @Test
    fun `get remembers the first PC when none was remembered`() = runTest {
        every { networkSource.get() } returns flowOf(listOf(firstDto, secondDto))
        coEvery { deviceDAO.get() } returns null
        coJustRun { deviceDAO.insert(any()) }

        repository.get().first()

        coVerify(exactly = 1) { deviceDAO.insert(firstDto.toDBO()) }
    }
}