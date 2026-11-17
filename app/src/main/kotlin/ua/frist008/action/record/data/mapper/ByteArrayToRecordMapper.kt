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
        val isStatus = size >= FIELDS_OFFSET &&
            ByteArrayToIsFromPcMapper(dataArr, size) &&
            dataArr[10] == NetworkConst.TYPE_STATUS

        if (!isStatus) return null

        val payloadLength = ByteArrayToNumberMapper(dataArr, PAYLOAD_OFFSET, Int.SIZE_BYTES)

        if (payloadLength <= 0) return null

        val end = (PAYLOAD_OFFSET + payloadLength).coerceAtMost(size.toLong()).toInt()
        var record = old.copy(isWebCamFrameReady = false)
        var fieldOffset = FIELDS_OFFSET

        while (fieldOffset + FIELD_HEADER_SIZE <= end) {
            val dataType = RecordDataType.parse(dataArr[fieldOffset])
            val valueLength = ByteArrayToNumberMapper(dataArr, fieldOffset + 1, Int.SIZE_BYTES)
            val valueOffset = fieldOffset + FIELD_HEADER_SIZE

            if (valueLength < 0 || valueOffset + valueLength > end) break

            record = dataType.map(dataArr, valueOffset, valueLength.toInt(), record)
            fieldOffset = valueOffset + valueLength.toInt()
        }

        return record
    }

    private const val PAYLOAD_OFFSET = 11
    private const val FIELDS_OFFSET = 15
    private const val FIELD_HEADER_SIZE = 5
}