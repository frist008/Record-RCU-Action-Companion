package ua.frist008.action.record.data.infrastructure.entity.dto.type

import timber.log.Timber
import ua.frist008.action.record.BuildConfig
import ua.frist008.action.record.data.infrastructure.entity.dto.RecordDTO
import ua.frist008.action.record.util.common.ByteConst
import ua.frist008.action.record.util.common.NetworkDataConst
import ua.frist008.action.record.util.io.mb
import java.io.UnsupportedEncodingException
import kotlin.time.Duration.Companion.seconds

sealed class RecordDataType<T>(val code: Byte) {

    abstract fun map(newData: T, old: RecordDTO): RecordDTO

    protected fun <P> log(old: P, new: P) {
        if (BuildConfig.DEBUG && old != new) {
            Timber.d("$code - old=${old}, new=$new; ${this::class.simpleName}")
        }
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

    data object UnknownDataType : RecordDataType<Long>(-1) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            Timber.e("$code - old=${old}, new=$newData; Unknown")
            return old
        }
    }

    data object RecordModeDataType : RecordDataType<Long>(11) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            val recordModeType = RecordModeType[newData.toInt()]
            log(old.recordModeType, recordModeType)
            return old.copy(connected = true, webCamType = 0, recordModeType = recordModeType)
        }
    }

    data object FpsDataType : RecordDataType<Long>(12) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            val newFps = newData.toInt()
            log(old.fps, newFps)
            return old.copy(fps = newFps)
        }
    }

    data object MaxFpsDataType : RecordDataType<Long>(22) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            val maxFps = newData.toInt()
            log(old.maxFps, maxFps)
            return old.copy(maxFps = maxFps)
        }
    }

    data object DurationDataType : RecordDataType<Long>(13) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            val duration = newData.seconds
            log(old.duration, duration)
            return old.copy(duration = duration)
        }
    }

    data object MaxDurationDataType : RecordDataType<Long>(14) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            val maxDuration = newData.seconds
            log(old.maxDuration.inWholeSeconds, maxDuration.inWholeSeconds)
            return old.copy(maxDuration = maxDuration)
        }
    }

    data object RecTooltipDataType : RecordDataType<ByteArray>(15) {
        override fun map(newData: ByteArray, old: RecordDTO): RecordDTO {
            try {
                val recTooltip = String(newData)
                log(old.recTooltip, recTooltip)
                return old.copy(recTooltip = recTooltip)
            } catch (e: UnsupportedEncodingException) {
                Timber.e(e)
                return old
            }
        }
    }

    data object EngineDataType : RecordDataType<Triple<ByteArray, Int, Int>>(16) {
        override fun map(newData: Triple<ByteArray, Int, Int>, old: RecordDTO): RecordDTO {
            val (data, position, typeParse) = newData
            var i = 0
            var engine = ""

            while (i < typeParse) {
                engine += Char(data[position + i++].toUShort())
            }

            log(old.engine, engine)
            return old.copy(engine = engine)
        }
    }

    data object StorageDataType : RecordDataType<Long>(17) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            val freeSpace = newData.mb
            log(old.freeSpace.mb, freeSpace.mb)
            return old.copy(freeSpace = freeSpace)
        }
    }

    data object StreamTypeDataType : RecordDataType<Long>(18) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            val streamType = StreamType[newData.toInt()]
            log(old.streamType, streamType)
            return old.copy(streamType = streamType)
        }
    }

    data object RecordOrStreamDataType : RecordDataType<Long>(19) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            val isStream = newData == 3L
            log(old.isStream, isStream)
            return old.copy(isStream = isStream)
        }
    }

    data object WebCamDataType : RecordDataType<Long>(20) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            val isWebCam = newData != 0L
            log(old.isWebCam, isWebCam)
            return old.copy(isWebCam = isWebCam)
        }
    }

    data object MicDataType : RecordDataType<Long>(21) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            val isMic = newData != 0L
            log(old.isMic, isMic)
            return old.copy(isMic = isMic)
        }
    }

    data object GameActiveDataType : RecordDataType<Long>(23) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            val gameActive = newData != 0L
            log(old.gameActive, gameActive)
            return old.copy(gameActive = gameActive)
        }
    }

    data object WebCamTypeDataType : RecordDataType<Long>(24) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            val webCamType = newData.toInt()
            log(old.webCamType, webCamType)
            return old.copy(webCamType = newData.toInt())
        }
    }

    data object WebDataDataType : RecordDataType<Long>(25) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO = old
    }
}

object Converter {

    fun map(data: ByteArray, position: Int, typeParse: Int): Long {
        return when (typeParse) {
            1 -> data[position].toLong()

            2 -> (((data[position].toInt() shl ByteConst.BITS_1) and NetworkDataConst.FF00) or
                (data[position + 1].toInt() and ByteConst.MAX_UBYTE)).toLong()

            4 -> ((data[position].toLong() shl ByteConst.BITS_3) and -ByteConst.MAX_BYTES_POW_3_LONG) or
                ((data[position + 1].toLong() shl ByteConst.BITS_2) and NetworkDataConst.FFFF_MINUS_1) or
                ((data[position + 2].toLong() shl ByteConst.BITS_1) and NetworkDataConst.FF00_LONG) or
                (data[position + 3].toLong() and ByteConst.MAX_UBYTE_LONG)

            else -> 0
        }
    }
}
