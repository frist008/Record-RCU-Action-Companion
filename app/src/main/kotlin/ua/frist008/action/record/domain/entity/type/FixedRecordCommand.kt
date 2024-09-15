package ua.frist008.action.record.domain.entity.type

enum class FixedRecordCommand(
    override val commandInt: Int,
    override val commandArr: ByteArray = byteArrayOf(commandInt.toByte()),
) : RecordCommand {

    START(1),
    RESUME(1),
    PAUSE(1),
    STOP(3),
}

enum class OriginalRecordCommand(
    override val commandInt: Int,
    override val commandArr: ByteArray = byteArrayOf(commandInt.toByte()),
) : RecordCommand {

    START(2),
    RESUME(2),
    PAUSE(1),
    STOP(3),
}

enum class StreamingRecordCommand(
    override val commandInt: Int,
    override val commandArr: ByteArray = byteArrayOf(commandInt.toByte()),
) : RecordCommand {

    START(1),
    STOP(3),
}

interface RecordCommand {
    val commandInt: Int
    val commandArr: ByteArray
}
