package ua.frist008.action.record.data.mapper

import ua.frist008.action.record.core.util.common.ByteConst

/**
 * Reads a big-endian number of the protocol: one byte signed, two bytes unsigned, four bytes
 * signed, and `0` for any other length, as the original app reads them.
 */
object ByteArrayToNumberMapper : (ByteArray, Int, Int) -> Long {

    override operator fun invoke(dataArr: ByteArray, offset: Int, length: Int): Long {
        val intLowestByte = 3 // the place of the lowest byte of a four-byte number

        return when (length) {
            Byte.SIZE_BYTES -> dataArr[offset].toLong()

            Short.SIZE_BYTES ->
                (
                    (readUnsigned(dataArr, offset) shl ByteConst.BITS_1) or
                        readUnsigned(dataArr, offset + 1)
                    ).toLong()

            Int.SIZE_BYTES ->
                (
                    (dataArr[offset].toInt() shl ByteConst.BITS_3) or
                        (readUnsigned(dataArr, offset + 1) shl ByteConst.BITS_2) or
                        (readUnsigned(dataArr, offset + 2) shl ByteConst.BITS_1) or
                        readUnsigned(dataArr, offset + intLowestByte)
                    ).toLong()

            else -> 0L
        }
    }

    private fun readUnsigned(dataArr: ByteArray, index: Int): Int =
        dataArr[index].toInt() and ByteConst.MAX_UBYTE
}
