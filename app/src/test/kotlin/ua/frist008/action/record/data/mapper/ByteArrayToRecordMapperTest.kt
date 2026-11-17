package ua.frist008.action.record.data.mapper

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import ua.frist008.action.record.data.network.record.entity.RecordDTO
import ua.frist008.action.record.data.network.record.entity.RecordModeType
import ua.frist008.action.record.data.network.record.entity.WebCamData
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

    /** Builds field 25: a chunk of two pixels of the frame [FRAME_ID] of camera 0, two by two */
    private fun buildWebCamChunk(chunkIndex: Int, pixelsArr: ByteArray): ByteArray {
        val headerArr = listOf(FRAME_ID, chunkIndex, 0, 2, 2, 2, pixelsArr.size)
            .fold(ByteArray(0)) { all, value -> all + buildInt(value) }

        return buildField(type = 25, valueArr = headerArr + pixelsArr)
    }

    @Test
    fun `status packet marks the session connected and reads fps`() {
        val dataArr = buildStatus(listOf(buildField(type = 12, valueArr = buildInt(60))))

        val record = ByteArrayToRecordMapper(dataArr, dataArr.size, disconnected)

        assertTrue(record?.connected == true)
        assertEquals(60, record?.fps)
    }

    @Test
    fun `reads record mode of one byte and marks the session connected`() {
        val dataArr = buildStatus(listOf(buildField(type = 11, valueArr = byteArrayOf(1))))

        val record = ByteArrayToRecordMapper(dataArr, dataArr.size, disconnected)

        assertEquals(RecordModeType.RECORD, record?.recordModeType)
        assertTrue(record?.connected == true)
    }

    @Test
    fun `status packet without record mode or fps leaves the session disconnected`() {
        val dataArr = buildStatus(listOf(buildField(type = 13, valueArr = buildInt(90))))

        val record = ByteArrayToRecordMapper(dataArr, dataArr.size, disconnected)

        assertFalse(record?.connected ?: true)
        assertEquals(90.seconds, record?.duration)
    }

    @Test
    fun `game is inactive until its field arrives`() {
        val fpsArr = buildField(type = 12, valueArr = buildInt(60))
        val firstArr = buildStatus(listOf(fpsArr))
        val secondArr =
            buildStatus(listOf(fpsArr, buildField(type = 23, valueArr = byteArrayOf(1))))

        val first = ByteArrayToRecordMapper(firstArr, firstArr.size, disconnected)
        val second = first?.let { record ->
            ByteArrayToRecordMapper(secondArr, secondArr.size, record)
        }

        assertFalse(first?.gameActive ?: true)
        assertTrue(second?.gameActive == true)
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
    fun `skips a chunk of a camera the PC does not stream and reads the next field`() {
        val webCamArr = buildField(type = 25, valueArr = ByteArray(40) { 0x7F })
        val fpsArr = buildField(type = 12, valueArr = buildInt(50))
        val dataArr = buildStatus(listOf(webCamArr, fpsArr))

        val record = ByteArrayToRecordMapper(dataArr, dataArr.size, disconnected)

        assertEquals(50, record?.fps)
        assertEquals(disconnected.webCamDataList, record?.webCamDataList)
    }

    @Test
    fun `assembles a frame of a web camera from its chunks`() {
        val camerasArr = buildField(type = 24, valueArr = buildInt(1))
        val firstArr = buildWebCamChunk(chunkIndex = 0, pixelsArr = byteArrayOf(1, 2, 3, 4))
        val secondArr = buildWebCamChunk(chunkIndex = 1, pixelsArr = byteArrayOf(5, 6, 7, 8))
        val dataArr = buildStatus(listOf(camerasArr, firstArr, secondArr))

        val record = ByteArrayToRecordMapper(dataArr, dataArr.size, disconnected)
        val webCam = record?.webCamDataList?.first()

        assertArrayEquals(byteArrayOf(1, 2, 3, 4, 5, 6, 7, 8), webCam?.g)
        assertEquals(FRAME_ID, webCam?.a)
        assertEquals(0, webCam?.b)
        assertEquals(2, webCam?.c)
        assertEquals(2, webCam?.d)
        assertTrue(record?.isWebCamFrameReady == true)
    }

    @Test
    fun `skips a chunk outside its frame and reads the field after it`() {
        val camerasArr = buildField(type = 24, valueArr = buildInt(1))
        val chunkArr = buildWebCamChunk(chunkIndex = 9, pixelsArr = byteArrayOf(1, 2, 3, 4))
        val fpsArr = buildField(type = 12, valueArr = buildInt(50))
        val dataArr = buildStatus(listOf(camerasArr, chunkArr, fpsArr))

        val record = ByteArrayToRecordMapper(dataArr, dataArr.size, disconnected)

        assertEquals(disconnected.webCamDataList, record?.webCamDataList)
        assertEquals(50, record?.fps)
    }

    @Test
    fun `skips a chunk of a frame older than the one assembled`() {
        val newerFrame = WebCamData(a = FRAME_ID + 1, f = ByteArray(8), g = ByteArray(8))
        val old = disconnected.copy(
            webCamType = 1,
            webCamDataList = listOf(newerFrame) + disconnected.webCamDataList.drop(1),
        )
        val chunkArr = buildWebCamChunk(chunkIndex = 0, pixelsArr = byteArrayOf(1, 2, 3, 4))
        val dataArr = buildStatus(listOf(chunkArr))

        val record = ByteArrayToRecordMapper(dataArr, dataArr.size, old)

        assertEquals(old.webCamDataList, record?.webCamDataList)
    }

    @Test
    fun `a new packet starts with no web camera frame ready`() {
        val old = disconnected.copy(isWebCamFrameReady = true)
        val dataArr = buildStatus(listOf(buildField(type = 13, valueArr = buildInt(90))))

        val record = ByteArrayToRecordMapper(dataArr, dataArr.size, old)

        assertFalse(record?.isWebCamFrameReady ?: true)
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

    companion object {

        private const val FRAME_ID = 5
    }
}
