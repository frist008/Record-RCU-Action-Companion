package ua.frist008.action.record.data.mapper

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ByteArrayToIsFromPcMapperTest {

    private fun buildHeader(key: String, sequenceHigh: Int, sequenceLow: Int): ByteArray =
        key.toByteArray(Charsets.US_ASCII) + sequenceHigh.toByte() + sequenceLow.toByte()

    @Test
    fun `header AH with sequence 32768 is from the PC`() {
        // 0x8000 = 32768
        val dataArr = buildHeader("AH", sequenceHigh = 0x80, sequenceLow = 0x00)

        assertTrue(ByteArrayToIsFromPcMapper(dataArr, dataArr.size))
    }

    @Test
    fun `header AH with sequence 32767 is from the phone`() {
        // 0x7FFF = 32767
        val dataArr = buildHeader("AH", sequenceHigh = 0x7F, sequenceLow = 0xFF)

        assertFalse(ByteArrayToIsFromPcMapper(dataArr, dataArr.size))
    }

    @Test
    fun `another header is not from the PC`() {
        val dataArr = buildHeader("AX", sequenceHigh = 0xFF, sequenceLow = 0xFF)

        assertFalse(ByteArrayToIsFromPcMapper(dataArr, dataArr.size))
    }

    @Test
    fun `packet shorter than the header is not from the PC`() {
        val dataArr = buildHeader("AH", sequenceHigh = 0xFF, sequenceLow = 0xFF)

        assertFalse(ByteArrayToIsFromPcMapper(dataArr, 3))
    }
}