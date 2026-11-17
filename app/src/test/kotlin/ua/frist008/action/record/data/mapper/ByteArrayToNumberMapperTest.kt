package ua.frist008.action.record.data.mapper

import org.junit.Assert.assertEquals
import org.junit.Test

class ByteArrayToNumberMapperTest {

    private fun buildBytes(vararg values: Int): ByteArray =
        ByteArray(values.size) { values[it].toByte() }

    @Test
    fun `one byte is read signed`() {
        assertEquals(-1L, ByteArrayToNumberMapper(buildBytes(0xFF), 0, 1))
    }

    @Test
    fun `two bytes are read unsigned`() {
        // 0xFFFF = 65535
        assertEquals(65_535L, ByteArrayToNumberMapper(buildBytes(0xFF, 0xFF), 0, 2))
    }

    @Test
    fun `four bytes are read big-endian`() {
        // 0x00010203 = 66051
        assertEquals(66_051L, ByteArrayToNumberMapper(buildBytes(0x00, 0x01, 0x02, 0x03), 0, 4))
    }

    @Test
    fun `four bytes are read signed`() {
        // 0xFFFFFFFE = -2 as Int
        assertEquals(-2L, ByteArrayToNumberMapper(buildBytes(0xFF, 0xFF, 0xFF, 0xFE), 0, 4))
    }

    @Test
    fun `four bytes above 127 in the lower bytes stay positive`() {
        // 0x0000C0A8 = 49320
        assertEquals(49_320L, ByteArrayToNumberMapper(buildBytes(0x00, 0x00, 0xC0, 0xA8), 0, 4))
    }

    @Test
    fun `reading starts at the offset`() {
        // 0x0102 = 258
        assertEquals(258L, ByteArrayToNumberMapper(buildBytes(0x7F, 0x01, 0x02), 1, 2))
    }

    @Test
    fun `any other length reads 0`() {
        assertEquals(0L, ByteArrayToNumberMapper(buildBytes(0x01, 0x02, 0x03), 0, 3))
    }
}
