package ua.frist008.action.record.data.infrastructure.mapper

import timber.log.Timber
import ua.frist008.action.record.util.common.ByteConst
import ua.frist008.action.record.util.common.NetworkDataConst
import javax.inject.Inject

class ByteArrayToNameMapper @Inject constructor() : (ByteArray, Int) -> String? {

    override fun invoke(dataArr: ByteArray, size: Int): String? {
        val namePCLong = ((dataArr[14].toLong()) and ByteConst.MAX_UBYTE_LONG) or
            (((dataArr[11].toLong()) shl ByteConst.BITS_3) and -ByteConst.MAX_BYTES_POW_3) or
            (((dataArr[12].toLong()) shl ByteConst.BITS_2) and NetworkDataConst.FFFF_PLUS_1) or
            (((dataArr[13].toLong()) shl ByteConst.BITS_1) and NetworkDataConst.FF00_LONG)
        val namePCByteSize = (if (15 + namePCLong <= size) namePCLong else size).toInt()
        val namePCByteArr = ByteArray(namePCByteSize + 1)
        namePCByteArr[namePCByteSize] = 0

        return if (namePCLong > 0) {
            System.arraycopy(dataArr, 15, namePCByteArr, 0, namePCByteSize)

            runCatching { String(namePCByteArr) }.onFailure(Timber::e).getOrNull()
        } else {
            null
        }
    }
}
