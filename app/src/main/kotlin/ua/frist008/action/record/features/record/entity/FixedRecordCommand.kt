package ua.frist008.action.record.features.record.entity

enum class FixedRecordCommand(
    override val commandInt: Int,
    override val commandArr: ByteArray = byteArrayOf(commandInt.toByte()),
) : RecordCommand {

    START(commandInt = 1),
    RESUME(commandInt = 1),
    PAUSE(commandInt = 1),
    STOP(commandInt = 3),
}

/** The codes the original app sends to start and resume a recording; no screen sends them yet */
enum class OriginalRecordCommand(
    override val commandInt: Int,
    override val commandArr: ByteArray = byteArrayOf(commandInt.toByte()),
) : RecordCommand {

    START(commandInt = 2),
    RESUME(commandInt = 2),
    PAUSE(commandInt = 1),
    STOP(commandInt = 3),
}

enum class StreamingRecordCommand(
    override val commandInt: Int,
    override val commandArr: ByteArray = byteArrayOf(commandInt.toByte()),
) : RecordCommand {

    START(commandInt = 1),
    STOP(commandInt = 3),
}

interface RecordCommand {
    val commandInt: Int
    val commandArr: ByteArray
}
