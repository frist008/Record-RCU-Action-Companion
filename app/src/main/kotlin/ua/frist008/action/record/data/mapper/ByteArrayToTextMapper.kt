package ua.frist008.action.record.data.mapper

import java.nio.charset.Charset

/** Reads a string of the protocol, without the trailing `\0` the PC ends it with */
object ByteArrayToTextMapper : (ByteArray, Int, Int, Charset) -> String {

    override operator fun invoke(
        dataArr: ByteArray,
        offset: Int,
        length: Int,
        charset: Charset,
    ): String {
        val endOfString = Char.MIN_VALUE

        return String(dataArr, offset, length, charset).trimEnd(endOfString)
    }
}
