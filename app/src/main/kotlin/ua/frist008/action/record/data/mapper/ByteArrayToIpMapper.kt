package ua.frist008.action.record.data.mapper

import ua.frist008.action.record.core.util.common.ByteConst
import ua.frist008.action.record.data.network.NetworkConst

/** Reads the IPv4 address of the PC from bytes 4–7 of a radar reply, where byte 4 is the lowest */
object ByteArrayToIpMapper : (ByteArray) -> Long {

    private const val LOWEST_BYTE = NetworkConst.ADDRESS_INDEX
    private const val SECOND_BYTE = NetworkConst.ADDRESS_INDEX + 1
    private const val THIRD_BYTE = NetworkConst.ADDRESS_INDEX + 2
    private const val HIGHEST_BYTE = NetworkConst.ADDRESS_INDEX + 3

    override operator fun invoke(dataArr: ByteArray): Long =
        (readUnsigned(dataArr, HIGHEST_BYTE) shl ByteConst.BITS_3) or
            (readUnsigned(dataArr, THIRD_BYTE) shl ByteConst.BITS_2) or
            (readUnsigned(dataArr, SECOND_BYTE) shl ByteConst.BITS_1) or
            readUnsigned(dataArr, LOWEST_BYTE)

    private fun readUnsigned(dataArr: ByteArray, index: Int): Long =
        dataArr[index].toLong() and ByteConst.MAX_UBYTE_LONG
}
