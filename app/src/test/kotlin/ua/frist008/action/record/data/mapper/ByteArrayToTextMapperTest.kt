package ua.frist008.action.record.data.mapper

import org.junit.Assert.assertEquals
import org.junit.Test

class ByteArrayToTextMapperTest {

    private fun buildBytesWithText(prefixSize: Int, text: String): ByteArray =
        ByteArray(prefixSize) + text.toByteArray(Charsets.UTF_8)

    @Test
    fun `reads UTF-8 text from the offset`() {
        val dataArr = buildBytesWithText(prefixSize = 3, text = "Мой ПК")

        val text = ByteArrayToTextMapper(dataArr, 3, dataArr.size - 3, Charsets.UTF_8)

        assertEquals("Мой ПК", text)
    }

    @Test
    fun `drops the trailing end of string`() {
        val dataArr = buildBytesWithText(prefixSize = 0, text = "Ready\u0000\u0000")

        assertEquals("Ready", ByteArrayToTextMapper(dataArr, 0, dataArr.size, Charsets.UTF_8))
    }

    @Test
    fun `reads only the given length`() {
        val dataArr = buildBytesWithText(prefixSize = 0, text = "DX11Vulkan")

        assertEquals("DX11", ByteArrayToTextMapper(dataArr, 0, 4, Charsets.ISO_8859_1))
    }

    @Test
    fun `zero length reads an empty string`() {
        assertEquals("", ByteArrayToTextMapper(ByteArray(4), 2, 0, Charsets.UTF_8))
    }
}
