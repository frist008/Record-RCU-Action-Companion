package ua.frist008.action.record.features.record.entity

enum class FixedRecordCommand(
    override val commandInt: Int,
    override val commandArr: ByteArray = byteArrayOf(commandInt.toByte()),
) : RecordCommand {

    START(1),
    RESUME(1),
    PAUSE(1),
    STOP(3),
}

/** The codes the original app sends to start and resume a recording; no screen sends them yet */
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
