package ua.frist008.action.record.data.mapper

import ua.frist008.action.record.data.network.NetworkConst
import ua.frist008.action.record.data.network.record.entity.RecordDTO
import ua.frist008.action.record.data.network.record.entity.RecordDataType

/**
 * Applies a status packet of the record session to the last state: bytes 11–14 hold the length of
 * the payload counted from byte 11, then come the fields — a type byte, four bytes of length and
 * the value; `null` when the packet is no status packet. The session counts as connected once a
 * field of the record mode or of FPS arrives, as the release 1.0.4.
 */
object ByteArrayToRecordMapper : (ByteArray, Int, RecordDTO) -> RecordDTO? {

    override operator fun invoke(dataArr: ByteArray, size: Int, old: RecordDTO): RecordDTO? {
        val payloadOffset = 11
        val fieldsOffset = 15
        val fieldHeaderSize = 5
        val typeStatus: Byte = 2 // the packet type in byte 10 of a status packet

        val isStatus = size >= fieldsOffset &&
            ByteArrayToIsFromPcMapper(dataArr, size) &&
            dataArr[NetworkConst.TYPE_INDEX] == typeStatus

        if (!isStatus) return null

        val payloadLength = ByteArrayToNumberMapper(dataArr, payloadOffset, Int.SIZE_BYTES)

        if (payloadLength <= 0) return null

        val end = (payloadOffset + payloadLength).coerceAtMost(size.toLong()).toInt()
        var record = old.copy(isWebCamFrameReady = false)
        var fieldOffset = fieldsOffset

        while (fieldOffset + fieldHeaderSize <= end) {
            val dataType = RecordDataType.parse(dataArr[fieldOffset])
            val valueLength = ByteArrayToNumberMapper(dataArr, fieldOffset + 1, Int.SIZE_BYTES)
            val valueOffset = fieldOffset + fieldHeaderSize

            if (valueLength < 0 || valueOffset + valueLength > end) break

            record = dataType.map(dataArr, valueOffset, valueLength.toInt(), record)
            fieldOffset = valueOffset + valueLength.toInt()
        }

        return record
    }
}
