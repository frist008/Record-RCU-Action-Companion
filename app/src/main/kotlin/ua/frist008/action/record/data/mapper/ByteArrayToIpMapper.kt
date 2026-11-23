package ua.frist008.action.record.data.mapper

import ua.frist008.action.record.core.util.common.ByteConst
import ua.frist008.action.record.data.network.NetworkConst

/** Reads the IPv4 address of the PC from bytes 4–7 of a radar reply, where byte 4 is the lowest */
object ByteArrayToIpMapper : (ByteArray) -> Long {

    override operator fun invoke(dataArr: ByteArray): Long {
        val lowestByte = NetworkConst.ADDRESS_INDEX
        val secondByte = NetworkConst.ADDRESS_INDEX + 1
        val thirdByte = NetworkConst.ADDRESS_INDEX + 2
        val highestByte = NetworkConst.ADDRESS_INDEX + 3

        return (readUnsigned(dataArr, highestByte) shl ByteConst.BITS_3) or
            (readUnsigned(dataArr, thirdByte) shl ByteConst.BITS_2) or
            (readUnsigned(dataArr, secondByte) shl ByteConst.BITS_1) or
            readUnsigned(dataArr, lowestByte)
    }

    private fun readUnsigned(dataArr: ByteArray, index: Int): Long =
        dataArr[index].toLong() and ByteConst.MAX_UBYTE_LONG
}
