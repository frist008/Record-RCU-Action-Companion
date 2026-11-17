package ua.frist008.action.record.data.mapper

import ua.frist008.action.record.data.network.NetworkConst

/**
 * Tells a packet of the PC by its header: the letters `AH`, then a sequence number of at least
 * 32768, while the phone numbers its own packets below it.
 */
object ByteArrayToIsFromPcMapper : (ByteArray, Int) -> Boolean {

    override operator fun invoke(dataArr: ByteArray, size: Int): Boolean =
        size >= HEADER_SIZE &&
            dataArr[0] == NetworkConst.KEY_0 &&
            dataArr[1] == NetworkConst.KEY_1 &&
            ByteArrayToNumberMapper(dataArr, 2, Short.SIZE_BYTES) > Short.MAX_VALUE // dataArr[2-3]

    private const val HEADER_SIZE = 4
}
