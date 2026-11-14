package ua.frist008.action.record.data.mapper

import ua.frist008.action.record.core.util.common.ByteConst

/** Reads the IPv4 address of the PC from bytes 4–7 of a radar reply, where byte 4 is the lowest */
object ByteArrayToIpMapper : (ByteArray) -> Long {

    override operator fun invoke(dataArr: ByteArray): Long =
        (readUnsigned(dataArr, 7) shl ByteConst.BITS_3) or
            (readUnsigned(dataArr, 6) shl ByteConst.BITS_2) or
            (readUnsigned(dataArr, 5) shl ByteConst.BITS_1) or
            readUnsigned(dataArr, 4)

    private fun readUnsigned(dataArr: ByteArray, index: Int): Long =
        dataArr[index].toLong() and ByteConst.MAX_UBYTE_LONG
}