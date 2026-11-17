package ua.frist008.action.record.data.network.record

import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ua.frist008.action.record.data.local.device.DeviceDAO
import ua.frist008.action.record.data.local.device.DeviceDBO
import ua.frist008.action.record.data.network.DisconnectException
import ua.frist008.action.record.data.network.NotConnectedException
import ua.frist008.action.record.data.network.record.entity.RecordDTO
import ua.frist008.action.record.features.record.entity.FixedRecordCommand

class RecordRepositoryImplTest {

    private val deviceDAO: DeviceDAO = mockk()
    private val recordNetworkSource: RecordNetworkSource = mockk()
    private lateinit var repository: RecordRepositoryImpl

    private val testDbo = DeviceDBO(name = "Test PC", ip = 16_909_060L, port = 2555)
    private val testDto = testDbo.toDTO()

    @Before
    fun setup() {
        repository = RecordRepositoryImpl(deviceDAO, recordNetworkSource)
    }

    @Test
    fun `connect throws DisconnectException when no PC is remembered`() = runTest {
        coEvery { deviceDAO.get() } returns null

        var thrown: Throwable? = null
        try {
            repository.connect()
        } catch (e: DisconnectException) {
            thrown = e
        }

        assertTrue(thrown is DisconnectException)
    }

    @Test
    fun `connect calls networkSource_connect with the remembered PC`() = runTest {
        coEvery { deviceDAO.get() } returns testDbo
        coJustRun { recordNetworkSource.connect(any()) }

        repository.connect()

        coVerify { recordNetworkSource.connect(testDto) }
    }

    @Test
    fun `recordFlow skips null and emits the domain entity of the DTO`() = runTest {
        val recordDto = RecordDTO(connected = true, fps = 60, gameActive = true)

        every { recordNetworkSource.recordFlow } returns flowOf(null, recordDto)

        val result = repository.recordFlow.first()

        assertEquals(recordDto.toDomain(), result)
    }

    @Test
    fun `recordFlow is not null on creation`() {
        every { recordNetworkSource.recordFlow } returns flowOf(null)
        assertNotNull(repository.recordFlow)
    }

    @Test
    fun `sendCommand passes the command to networkSource`() = runTest {
        coJustRun { recordNetworkSource.sendCommand(any()) }

        repository.sendCommand(FixedRecordCommand.STOP)

        coVerify { recordNetworkSource.sendCommand(FixedRecordCommand.STOP) }
    }

    @Test
    fun `sendCommand throws NotConnectedException without a session`() = runTest {
        coEvery { recordNetworkSource.sendCommand(any()) } throws NotConnectedException()

        var thrown: Throwable? = null
        try {
            repository.sendCommand(FixedRecordCommand.START)
        } catch (e: NotConnectedException) {
            thrown = e
        }

        assertTrue(thrown is NotConnectedException)
    }
}
