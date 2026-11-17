package ua.frist008.action.record.data.mapper

import ua.frist008.action.record.data.network.NetworkConst

/** Reads the TCP port of the PC from bytes 8–9 of a radar reply */
object ByteArrayToPortMapper : (ByteArray) -> Int {

    override operator fun invoke(dataArr: ByteArray): Int =
        ByteArrayToNumberMapper(dataArr, NetworkConst.PORT_INDEX, Short.SIZE_BYTES).toInt()
}
