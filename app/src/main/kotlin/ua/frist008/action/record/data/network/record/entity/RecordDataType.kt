package ua.frist008.action.record.data.network.record.entity

import timber.log.Timber
import ua.frist008.action.record.BuildConfig
import ua.frist008.action.record.core.util.io.mb
import ua.frist008.action.record.data.mapper.ByteArrayToNumberMapper
import ua.frist008.action.record.data.mapper.ByteArrayToTextMapper
import kotlin.time.Duration.Companion.seconds

sealed class RecordDataType<T>(val code: Byte) {

    abstract fun map(newData: T, old: RecordDTO): RecordDTO

    /**
     * Reads the value of the field from the status packet.
     *
     * @param dataArr the bytes of the status packet.
     * @param offset the index of the first byte of the value.
     * @param length the length of the value the packet declares, within the packet.
     * @return the value of the field.
     */
    protected abstract fun read(dataArr: ByteArray, offset: Int, length: Int): T

    /**
     * Applies the field of a status packet to the last state.
     *
     * @param dataArr the bytes of the status packet.
     * @param offset the index of the first byte of the value.
     * @param length the length of the value the packet declares, within the packet.
     * @param old the state before the field.
     * @return the state with the field applied.
     */
    fun map(dataArr: ByteArray, offset: Int, length: Int, old: RecordDTO): RecordDTO =
        map(read(dataArr, offset, length), old)

    protected fun <P> log(old: P, new: P) {
        if (BuildConfig.DEBUG && old != new) {
            Timber.d("$code - old=${old}, new=$new; ${this::class.simpleName}")
        }
    }

    /** A field whose value is a number of one, two or four bytes */
    sealed class NumberDataType(code: Byte) : RecordDataType<Long>(code) {

        override fun read(dataArr: ByteArray, offset: Int, length: Int): Long =
            ByteArrayToNumberMapper(dataArr, offset, length)
    }

    companion object {

        fun parse(code: Byte): RecordDataType<*> =
            values().firstOrNull { it.code == code } ?: UnknownDataType

        private fun values(): Array<RecordDataType<*>> = arrayOf(
            UnknownDataType,
            RecordModeDataType,
            FpsDataType,
            DurationDataType,
            MaxDurationDataType,
            RecTooltipDataType,
            EngineDataType,
            StorageDataType,
            StreamTypeDataType,
            RecordOrStreamDataType,
            WebCamDataType,
            MicDataType,
            MaxFpsDataType,
            GameActiveDataType,
            WebCamTypeDataType,
            WebDataDataType,
        )
    }

    data object UnknownDataType : NumberDataType(-1) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            Timber.e("$code - old=${old}, new=$newData; Unknown")
            return old
        }
    }

    data object RecordModeDataType : NumberDataType(11) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            val recordModeType = RecordModeType[newData.toInt()]
            log(old.recordModeType, recordModeType)
            return old.copy(webCamType = 0, recordModeType = recordModeType)
        }
    }

    data object FpsDataType : NumberDataType(12) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            val newFps = newData.toInt()
            log(old.fps, newFps)
            return old.copy(fps = newFps)
        }
    }

    data object MaxFpsDataType : NumberDataType(22) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            val maxFps = newData.toInt()
            log(old.maxFps, maxFps)
            return old.copy(maxFps = maxFps)
        }
    }

    data object DurationDataType : NumberDataType(13) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            val duration = newData.seconds
            log(old.duration, duration)
            return old.copy(duration = duration)
        }
    }

    data object MaxDurationDataType : NumberDataType(14) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            val maxDuration = newData.seconds
            log(old.maxDuration.inWholeSeconds, maxDuration.inWholeSeconds)
            return old.copy(maxDuration = maxDuration)
        }
    }

    data object RecTooltipDataType : RecordDataType<String>(15) {

        override fun read(dataArr: ByteArray, offset: Int, length: Int): String {
            val textLength = length.coerceAtMost(MAX_SIZE)

            return ByteArrayToTextMapper(dataArr, offset, textLength, Charsets.UTF_8)
        }

        override fun map(newData: String, old: RecordDTO): RecordDTO {
            log(old.recTooltip, newData)
            return old.copy(recTooltip = newData)
        }

        private const val MAX_SIZE = 1023
    }

    data object EngineDataType : RecordDataType<String>(16) {

        override fun read(dataArr: ByteArray, offset: Int, length: Int): String =
            ByteArrayToTextMapper(dataArr, offset, length, Charsets.ISO_8859_1)

        override fun map(newData: String, old: RecordDTO): RecordDTO {
            log(old.engine, newData)
            return old.copy(engine = newData)
        }
    }

    data object StorageDataType : NumberDataType(17) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            val freeSpace = newData.mb
            log(old.freeSpace.mb, freeSpace.mb)
            return old.copy(freeSpace = freeSpace)
        }
    }

    data object StreamTypeDataType : NumberDataType(18) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            val streamType = StreamType[newData.toInt()]
            log(old.streamType, streamType)
            return old.copy(streamType = streamType)
        }
    }

    data object RecordOrStreamDataType : NumberDataType(19) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            val isStream = newData == 3L
            log(old.isStream, isStream)
            return old.copy(isStream = isStream)
        }
    }

    data object WebCamDataType : NumberDataType(20) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            val isWebCam = newData != 0L
            log(old.isWebCam, isWebCam)
            return old.copy(isWebCam = isWebCam)
        }
    }

    data object MicDataType : NumberDataType(21) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            val isMic = newData != 0L
            log(old.isMic, isMic)
            return old.copy(isMic = isMic)
        }
    }

    data object GameActiveDataType : NumberDataType(23) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            val gameActive = newData != 0L
            log(old.gameActive, gameActive)
            return old.copy(gameActive = gameActive)
        }
    }

    data object WebCamTypeDataType : NumberDataType(24) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            val webCamType = newData.toInt()
            log(old.webCamType, webCamType)
            return old.copy(webCamType = newData.toInt())
        }
    }

    /** A chunk of a web camera frame, skipped until the previews of version 1.2.0 */
    data object WebDataDataType : RecordDataType<Unit>(25) {

        override fun read(dataArr: ByteArray, offset: Int, length: Int) = Unit

        override fun map(newData: Unit, old: RecordDTO): RecordDTO = old
    }
}