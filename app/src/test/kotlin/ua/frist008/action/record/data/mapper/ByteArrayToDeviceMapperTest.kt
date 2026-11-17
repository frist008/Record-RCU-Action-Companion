package ua.frist008.action.record.data.mapper

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import ua.frist008.action.record.data.network.device.entity.DeviceDTO

class ByteArrayToDeviceMapperTest {

    /** Builds a reply of the PC 192.168.0.13 on port 2555, its IPv4 reversed in bytes 4–7 */
    private fun buildRadarReply(type: Int, name: String, sequenceHigh: Int = 0x80): ByteArray {
        val nameArr = name.toByteArray(Charsets.UTF_8)
        val headerArr = "AH".toByteArray(Charsets.US_ASCII) + sequenceHigh.toByte() + 0x01.toByte()
        val addressArr = byteArrayOf(13, 0, 168.toByte(), 192.toByte(), 0x09, 0xFB.toByte())
        val nameLengthArr = byteArrayOf(0, 0, 0, nameArr.size.toByte())

        return headerArr + addressArr + type.toByte() + nameLengthArr + nameArr
    }

    @Test
    fun `reads ip, port and name of the reply`() {
        val dataArr = buildRadarReply(type = 1, name = "Game PC\u0000")

        val dto = ByteArrayToDeviceMapper(dataArr, dataArr.size)

        // 192.168.0.13 = 0xC0A8000D = 3232235533; 2555 = 0x09FB
        assertEquals(DeviceDTO(name = "Game PC", ip = 3232235533L, port = 2555), dto)
    }

    @Test
    fun `reply of another type has no name`() {
        val dataArr = buildRadarReply(type = 2, name = "Game PC")

        assertNull(ByteArrayToDeviceMapper(dataArr, dataArr.size)?.name)
    }

    @Test
    fun `reply of 10 bytes has address and no name`() {
        val dataArr = buildRadarReply(type = 1, name = "Game PC")

        val dto = ByteArrayToDeviceMapper(dataArr, 10)

        assertEquals(DeviceDTO(name = null, ip = 3232235533L, port = 2555), dto)
    }

    @Test
    fun `packet shorter than 10 bytes is no reply`() {
        val dataArr = buildRadarReply(type = 1, name = "Game PC")

        assertNull(ByteArrayToDeviceMapper(dataArr, 9))
    }

    @Test
    fun `request of the phone is no reply`() {
        val dataArr = buildRadarReply(type = 1, name = "Game PC", sequenceHigh = 0x7F)

        assertNull(ByteArrayToDeviceMapper(dataArr, dataArr.size))
    }
}