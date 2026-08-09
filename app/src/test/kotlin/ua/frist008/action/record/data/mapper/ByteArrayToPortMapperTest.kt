package ua.frist008.action.record.data.mapper

import org.junit.Assert.assertEquals
import org.junit.Test

class ByteArrayToPortMapperTest {

    private fun byteArrayWithPort(high: Int, low: Int): ByteArray =
        ByteArray(16).also { arr ->
            arr[8] = high.toByte()
            arr[9] = low.toByte()
        }

    @Test
    fun `zero bytes produce port 0`() {
        assertEquals(0, ByteArrayToPortMapper(ByteArray(16)))
    }

    @Test
    fun `port 80`() {
        assertEquals(80, ByteArrayToPortMapper(byteArrayWithPort(high = 0, low = 80)))
    }

    @Test
    fun `port 443`() {
        // 443 = 0x01BB: high=0x01=1, low=0xBB=187
        assertEquals(443, ByteArrayToPortMapper(byteArrayWithPort(high = 1, low = 0xBB)))
    }

    @Test
    fun `port 1234`() {
        // 1234 = 0x04D2: high=0x04=4, low=0xD2=210
        assertEquals(1234, ByteArrayToPortMapper(byteArrayWithPort(high = 4, low = 0xD2)))
    }

    @Test
    fun `port 2555 typical app port`() {
        // 2555 = 0x09FB: high=0x09=9, low=0xFB=251
        assertEquals(2555, ByteArrayToPortMapper(byteArrayWithPort(high = 9, low = 0xFB)))
    }

    @Test
    fun `port 65535 max`() {
        // 65535 = 0xFFFF: high=255, low=255
        assertEquals(65535, ByteArrayToPortMapper(byteArrayWithPort(high = 0xFF, low = 0xFF)))
    }
}
