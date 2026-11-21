package ua.frist008.action.record.data.mapper

import ua.frist008.action.record.data.network.NetworkConst

/**
 * Tells a packet of the PC by its header: the letters `AH`, then a sequence number of at least
 * 32768, while the phone numbers its own packets below it.
 */
object ByteArrayToIsFromPcMapper : (ByteArray, Int) -> Boolean {

    override operator fun invoke(dataArr: ByteArray, size: Int): Boolean {
        val headerSize = 4

        return size >= headerSize &&
            dataArr[0] == NetworkConst.KEY_0 &&
            dataArr[1] == NetworkConst.KEY_1 &&
            ByteArrayToNumberMapper(
                dataArr = dataArr,
                offset = NetworkConst.SEQUENCE_INDEX,
                length = Short.SIZE_BYTES,
            ) > Short.MAX_VALUE
    }
}
