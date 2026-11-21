package ua.frist008.action.record.data.mapper

/**
 * Reads the name of the PC from a radar reply: its length in bytes 11–14, its UTF-8 bytes from
 * byte 15 on, cut at the end of the packet; `null` when it is absent or empty.
 */
object ByteArrayToNameMapper : (ByteArray, Int) -> String? {

    override operator fun invoke(dataArr: ByteArray, size: Int): String? {
        val lengthIndex = 11
        val nameOffset = lengthIndex + Int.SIZE_BYTES

        if (size < nameOffset) return null

        val declaredLength = ByteArrayToNumberMapper(dataArr, lengthIndex, Int.SIZE_BYTES)

        if (declaredLength <= 0) return null

        val length = declaredLength.coerceAtMost((size - nameOffset).toLong()).toInt()
        val name = ByteArrayToTextMapper(dataArr, nameOffset, length, Charsets.UTF_8)

        return name.ifEmpty { null }
    }
}
