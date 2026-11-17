package ua.frist008.action.record.data.mapper

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import ua.frist008.action.record.data.network.record.entity.RecordDTO
import ua.frist008.action.record.data.network.record.entity.RecordModeType
import kotlin.time.Duration.Companion.seconds

class ByteArrayToRecordMapperTest {

    private val disconnected = RecordDTO(connected = false)

    private fun buildInt(value: Int): ByteArray =
        ByteArray(Int.SIZE_BYTES) { index -> (value shr (24 - index * 8)).toByte() }

    private fun buildField(type: Int, valueArr: ByteArray): ByteArray =
        byteArrayOf(type.toByte()) + buildInt(valueArr.size) + valueArr

    /** Builds a status packet whose bytes 11–14 count the payload from byte 11 */
    private fun buildStatus(fieldList: List<ByteArray>, packetType: Int = 2): ByteArray {
        val fieldsArr = fieldList.fold(ByteArray(0)) { all, field -> all + field }
        val headerArr = "AH".toByteArray(Charsets.US_ASCII) + 0x80.toByte() + 0x01.toByte()
        val payloadLengthArr = buildInt(fieldsArr.size + Int.SIZE_BYTES)

        return headerArr + ByteArray(6) + packetType.toByte() + payloadLengthArr + fieldsArr
    }

    @Test
    fun `status packet marks the session connected and reads fps`() {
        val dataArr = buildStatus(listOf(buildField(type = 12, valueArr = buildInt(60))))

        val record = ByteArrayToRecordMapper(dataArr, dataArr.size, disconnected)

        assertTrue(record?.connected == true)
        assertEquals(60, record?.fps)
    }

    @Test
    fun `reads record mode of one byte`() {
        val dataArr = buildStatus(listOf(buildField(type = 11, valueArr = byteArrayOf(1))))

        val record = ByteArrayToRecordMapper(dataArr, dataArr.size, disconnected)

        assertEquals(RecordModeType.RECORD, record?.recordModeType)
    }

    @Test
    fun `reads tooltip and engine without the trailing end of string`() {
        val tooltipArr = buildField(type = 15, valueArr = "Ready\u0000".toByteArray())
        val engineArr = buildField(type = 16, valueArr = "DX11\u0000".toByteArray())
        val dataArr = buildStatus(listOf(tooltipArr, engineArr))

        val record = ByteArrayToRecordMapper(dataArr, dataArr.size, disconnected)

        assertEquals("Ready", record?.recTooltip)
        assertEquals("DX11", record?.engine)
    }

    @Test
    fun `skips a web camera chunk and reads the field after it`() {
        val webCamArr = buildField(type = 25, valueArr = ByteArray(40) { 0x7F })
        val fpsArr = buildField(type = 12, valueArr = buildInt(50))
        val dataArr = buildStatus(listOf(webCamArr, fpsArr))

        val record = ByteArrayToRecordMapper(dataArr, dataArr.size, disconnected)

        assertEquals(50, record?.fps)
    }

    @Test
    fun `stops at a field longer than its payload`() {
        val fpsArr = buildField(type = 12, valueArr = buildInt(30))
        val truncatedDurationArr = byteArrayOf(13) + buildInt(100) + buildInt(3600)
        val dataArr = buildStatus(listOf(fpsArr, truncatedDurationArr))

        val record = ByteArrayToRecordMapper(dataArr, dataArr.size, disconnected)

        assertEquals(30, record?.fps)
        assertEquals(0.seconds, record?.duration)
    }

    @Test
    fun `reads no further than the size of the packet`() {
        val fpsArr = buildField(type = 12, valueArr = buildInt(30))
        val durationArr = buildField(type = 13, valueArr = buildInt(3600))
        val dataArr = buildStatus(listOf(fpsArr, durationArr))
        val sizeWithoutDuration = dataArr.size - durationArr.size

        val record = ByteArrayToRecordMapper(dataArr, sizeWithoutDuration, disconnected)

        assertEquals(30, record?.fps)
        assertEquals(0.seconds, record?.duration)
    }

    @Test
    fun `keeps the fields the packet does not carry`() {
        val old = RecordDTO(connected = true, engine = "Vulkan", fps = 144)
        val dataArr = buildStatus(listOf(buildField(type = 13, valueArr = buildInt(90))))

        val record = ByteArrayToRecordMapper(dataArr, dataArr.size, old)

        assertEquals(old.copy(duration = 90.seconds), record)
    }

    @Test
    fun `radar reply is no status packet`() {
        val fpsArr = buildField(type = 12, valueArr = buildInt(60))
        val dataArr = buildStatus(listOf(fpsArr), packetType = 1)

        assertNull(ByteArrayToRecordMapper(dataArr, dataArr.size, disconnected))
    }

    @Test
    fun `status packet with an empty payload is skipped`() {
        val dataArr = buildStatus(emptyList())

        dataArr.fill(0, fromIndex = 11, toIndex = 15)

        assertNull(ByteArrayToRecordMapper(dataArr, dataArr.size, disconnected))
    }
}