package ua.frist008.action.record.data.mapper

import ua.frist008.action.record.core.util.common.ByteConst

/**
 * Reads a big-endian number of the protocol: one byte signed, two bytes unsigned, four bytes
 * signed, and `0` for any other length, as the original app reads them.
 */
object ByteArrayToNumberMapper : (ByteArray, Int, Int) -> Long {

    /** The place of the lowest byte of a four-byte number, from its first byte */
    private const val INT_LOWEST_BYTE = 3

    override operator fun invoke(dataArr: ByteArray, offset: Int, length: Int): Long =
        when (length) {
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
                        readUnsigned(dataArr, offset + INT_LOWEST_BYTE)
                    ).toLong()

            else -> 0L
        }

    private fun readUnsigned(dataArr: ByteArray, index: Int): Int =
        dataArr[index].toInt() and ByteConst.MAX_UBYTE
}
