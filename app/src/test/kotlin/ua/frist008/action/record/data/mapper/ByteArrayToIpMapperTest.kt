package ua.frist008.action.record.data.mapper

import org.junit.Assert.assertEquals
import org.junit.Test

class ByteArrayToIpMapperTest {

    private fun byteArrayWithIp(b4: Int, b5: Int, b6: Int, b7: Int): ByteArray =
        ByteArray(16).also { arr ->
            arr[4] = b4.toByte()
            arr[5] = b5.toByte()
            arr[6] = b6.toByte()
            arr[7] = b7.toByte()
        }

    @Test
    fun `all zeros produce 0_0_0_0`() {
        val ip = ByteArrayToIpMapper(ByteArray(16))
        assertEquals("0.0.0.0", IpLongToStringMapper(ip))
    }

    @Test
    fun `byte4 only affects last octet`() {
        val ip = ByteArrayToIpMapper(byteArrayWithIp(b4 = 1, b5 = 0, b6 = 0, b7 = 0))
        assertEquals("0.0.0.1", IpLongToStringMapper(ip))
    }

    @Test
    fun `byte7 only affects first octet`() {
        val ip = ByteArrayToIpMapper(byteArrayWithIp(b4 = 0, b5 = 0, b6 = 0, b7 = 1))
        assertEquals("1.0.0.0", IpLongToStringMapper(ip))
    }

    @Test
    fun `maps 1_2_3_4 correctly`() {
        // byte[7]=1 (MSB), byte[6]=2, byte[5]=3, byte[4]=4 (LSB)
        val ip = ByteArrayToIpMapper(byteArrayWithIp(b4 = 4, b5 = 3, b6 = 2, b7 = 1))
        assertEquals("1.2.3.4", IpLongToStringMapper(ip))
    }

    @Test
    fun `handles unsigned byte values above 127`() {
        // 192.168.0.1: byte[7]=192(-64 signed), byte[6]=168(-88), byte[5]=0, byte[4]=1
        val ip = ByteArrayToIpMapper(byteArrayWithIp(b4 = 1, b5 = 0, b6 = 168, b7 = 192))
        assertEquals("192.168.0.1", IpLongToStringMapper(ip))
    }
}
