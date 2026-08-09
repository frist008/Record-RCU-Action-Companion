package ua.frist008.action.record.data.network.device

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ua.frist008.action.record.data.local.device.DeviceDAO
import ua.frist008.action.record.data.local.device.DeviceDBO
import ua.frist008.action.record.data.network.device.entity.DeviceDTO

class DeviceRadarRepositoryImplTest {

    private val deviceDAO: DeviceDAO = mockk()
    private val networkSource: DeviceRadarNetworkSource = mockk()
    private lateinit var repository: DeviceRadarRepositoryImpl

    @Before
    fun setup() {
        repository = DeviceRadarRepositoryImpl(deviceDAO, networkSource)
    }

    @Test
    fun `get returns empty list when DAO has no devices and no new DTO`() = runTest {
        coEvery { networkSource.get() } returns flowOf(null)
        every { deviceDAO.getAll() } returns flowOf(emptyList())

        val result = repository.get().first()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `get marks device as available when DTO ip matches stored ip`() = runTest {
        val ip = 16909060L
        val dto = DeviceDTO(name = "Test PC", ip = ip, port = 2555)
        val dbo = DeviceDBO(id = 1L, name = "Test PC", ip = ip, port = 2555)

        coEvery { networkSource.get() } returns flowOf(dto)
        coEvery { deviceDAO.insert(any()) } returns Unit
        every { deviceDAO.getAll() } returns flowOf(listOf(dbo))

        val result = repository.get().first()

        assertEquals(1, result.size)
        assertTrue(result[0].isAvailableStatus)
        assertEquals("Test PC", result[0].name)
    }

    @Test
    fun `get marks device as unavailable when DTO ip does not match`() = runTest {
        val storedIp = 16909060L
        val newIp = 16909061L
        val dto = DeviceDTO(name = "Other PC", ip = newIp, port = 2555)
        val dbo = DeviceDBO(id = 1L, name = "Stored PC", ip = storedIp, port = 2555)

        coEvery { networkSource.get() } returns flowOf(dto)
        coEvery { deviceDAO.insert(any()) } returns Unit
        every { deviceDAO.getAll() } returns flowOf(listOf(dbo))

        val result = repository.get().first()

        assertEquals(1, result.size)
        assertFalse(result[0].isAvailableStatus)
    }

    @Test
    fun `get inserts new DTO into database`() = runTest {
        val ip = 16909060L
        val dto = DeviceDTO(name = "New PC", ip = ip, port = 2555)

        coEvery { networkSource.get() } returns flowOf(dto)
        coEvery { deviceDAO.insert(any()) } returns Unit
        every { deviceDAO.getAll() } returns flowOf(emptyList())

        repository.get().first()

        coVerify { deviceDAO.insert(dto.toDBO()) }
    }

    @Test
    fun `get does not insert when DTO is null`() = runTest {
        coEvery { networkSource.get() } returns flowOf(null)
        every { deviceDAO.getAll() } returns flowOf(emptyList())

        repository.get().first()

        coVerify(exactly = 0) { deviceDAO.insert(any()) }
    }
}
