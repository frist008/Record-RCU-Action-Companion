package ua.frist008.action.record.data.infrastructure.mapper

import ua.frist008.action.record.util.common.ByteConst
import ua.frist008.action.record.util.common.NetworkDataConst
import javax.inject.Inject

class ByteArrayToIpMapper @Inject constructor() : (ByteArray) -> Long {

    override fun invoke(dataArr: ByteArray): Long =
        ((dataArr[7].toLong() shl ByteConst.BITS_3) and -ByteConst.MAX_BYTES_POW_3) or
            ((dataArr[6].toLong() shl ByteConst.BITS_2) and NetworkDataConst.FFFF_PLUS_1) or
            ((dataArr[5].toLong() shl ByteConst.BITS_1) and NetworkDataConst.FF00_LONG) or
            (dataArr[4].toLong() and ByteConst.MAX_UBYTE_LONG)
}
