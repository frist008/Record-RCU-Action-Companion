package ua.frist008.action.record.data.mapper

import ua.frist008.action.record.core.util.common.ByteConst
import ua.frist008.action.record.data.network.NetworkConst

object ByteArrayToPortMapper : (ByteArray) -> Int {

    override operator fun invoke(dataArr: ByteArray): Int =
        ((dataArr[8].toInt() shl ByteConst.BITS_1) and NetworkConst.FF00) or
            (dataArr[9].toInt() and ByteConst.MAX_UBYTE)
}
