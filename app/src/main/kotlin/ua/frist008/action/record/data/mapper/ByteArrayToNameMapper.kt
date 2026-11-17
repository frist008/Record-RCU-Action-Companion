package ua.frist008.action.record.data.mapper

/**
 * Reads the name of the PC from a radar reply: its length in bytes 11–14, its UTF-8 bytes from
 * byte 15 on, cut at the end of the packet; `null` when it is absent or empty.
 */
object ByteArrayToNameMapper : (ByteArray, Int) -> String? {

    private const val LENGTH_INDEX = 11
    private const val NAME_OFFSET = LENGTH_INDEX + Int.SIZE_BYTES

    override operator fun invoke(dataArr: ByteArray, size: Int): String? {
        if (size < NAME_OFFSET) return null

        val declaredLength = ByteArrayToNumberMapper(dataArr, LENGTH_INDEX, Int.SIZE_BYTES)

        if (declaredLength <= 0) return null

        val length = declaredLength.coerceAtMost((size - NAME_OFFSET).toLong()).toInt()
        val name = ByteArrayToTextMapper(dataArr, NAME_OFFSET, length, Charsets.UTF_8)

        return name.ifEmpty { null }
    }
}
