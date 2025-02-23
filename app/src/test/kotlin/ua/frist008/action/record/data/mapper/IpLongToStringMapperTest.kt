package ua.frist008.action.record.data.mapper

import org.junit.Assert.assertEquals
import org.junit.Test

class IpLongToStringMapperTest {

    @Test
    fun `maps zero to 0_0_0_0`() {
        assertEquals("0.0.0.0", IpLongToStringMapper(0L))
    }

    @Test
    fun `maps single LSB to last octet`() {
        assertEquals("0.0.0.1", IpLongToStringMapper(1L))
    }

    @Test
    fun `maps single MSB to first octet`() {
        // 1 shl 24 = 16777216
        assertEquals("1.0.0.0", IpLongToStringMapper(16777216L))
    }

    @Test
    fun `maps 1_2_3_4`() {
        // 0x01020304 = 16909060
        assertEquals("1.2.3.4", IpLongToStringMapper(16909060L))
    }

    @Test
    fun `maps max octets 255_255_255_255`() {
        // 0xFFFFFFFF = 4294967295... but as Long with masking:
        // 255 shl 24 = 4278190080, 255 shl 16 = 16711680, 255 shl 8 = 65280, 255
        val ip = (255L shl 24) or (255L shl 16) or (255L shl 8) or 255L
        assertEquals("255.255.255.255", IpLongToStringMapper(ip))
    }

    @Test
    fun `consistent with Long_toIPStr extension`() {
        val ip = 16909060L
        assertEquals(IpLongToStringMapper(ip), ip.toIPStr)
    }
}
