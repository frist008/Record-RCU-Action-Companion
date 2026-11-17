package ua.frist008.action.record.data.mapper

/** Reads the TCP port of the PC from bytes 8–9 of a radar reply */
object ByteArrayToPortMapper : (ByteArray) -> Int {

    override operator fun invoke(dataArr: ByteArray): Int =
        ByteArrayToNumberMapper(dataArr, 8, Short.SIZE_BYTES).toInt() // dataArr[8-9]
}