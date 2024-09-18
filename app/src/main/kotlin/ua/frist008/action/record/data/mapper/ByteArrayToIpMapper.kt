package ua.frist008.action.record.data.mapper

import ua.frist008.action.record.core.util.common.ByteConst
import ua.frist008.action.record.data.network.NetworkConst

object ByteArrayToIpMapper : (ByteArray) -> Long {

    override operator fun invoke(dataArr: ByteArray): Long =
        ((dataArr[7].toLong() shl ByteConst.BITS_3) and -ByteConst.MAX_BYTES_POW_3_LONG) or
            ((dataArr[6].toLong() shl ByteConst.BITS_2) and NetworkConst.FFFF_MINUS_1) or
            ((dataArr[5].toLong() shl ByteConst.BITS_1) and NetworkConst.FF00_LONG) or
            (dataArr[4].toLong() and ByteConst.MAX_UBYTE_LONG)
}
