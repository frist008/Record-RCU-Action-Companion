package ua.frist008.action.record.data.infrastructure.mapper

import ua.frist008.action.record.util.common.ByteConst
import ua.frist008.action.record.util.common.NetworkDataConst
import javax.inject.Inject

class ByteArrayToPortMapper @Inject constructor() : (ByteArray) -> Int {

    override fun invoke(dataArr: ByteArray): Int =
        ((dataArr[8].toInt() shl ByteConst.BITS_1) and NetworkDataConst.FF00) or
            (dataArr[9].toInt() and ByteConst.MAX_UBYTE)
}
