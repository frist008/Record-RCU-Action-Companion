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
            Timber.d("$code - old=$old, new=$new; ${this::class.simpleName}")
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

    data object UnknownDataType : NumberDataType(code = -1) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            Timber.e("$code - old=$old, new=$newData; Unknown")
            return old
        }
    }

    data object RecordModeDataType : NumberDataType(code = 11) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            val recordModeType = RecordModeType[newData.toInt()]
            log(old.recordModeType, recordModeType)
            return old.copy(connected = true, webCamType = 0, recordModeType = recordModeType)
        }
    }

    data object FpsDataType : NumberDataType(code = 12) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            val newFps = newData.toInt()
            log(old.fps, newFps)
            return old.copy(connected = true, fps = newFps)
        }
    }

    data object MaxFpsDataType : NumberDataType(code = 22) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            val maxFps = newData.toInt()
            log(old.maxFps, maxFps)
            return old.copy(maxFps = maxFps)
        }
    }

    data object DurationDataType : NumberDataType(code = 13) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            val duration = newData.seconds
            log(old.duration, duration)
            return old.copy(duration = duration)
        }
    }

    data object MaxDurationDataType : NumberDataType(code = 14) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            val maxDuration = newData.seconds
            log(old.maxDuration.inWholeSeconds, maxDuration.inWholeSeconds)
            return old.copy(maxDuration = maxDuration)
        }
    }

    data object RecTooltipDataType : RecordDataType<String>(code = 15) {

        override fun read(dataArr: ByteArray, offset: Int, length: Int): String {
            val maxSize = 1023
            val textLength = length.coerceAtMost(maxSize)

            return ByteArrayToTextMapper(dataArr, offset, textLength, Charsets.UTF_8)
        }

        override fun map(newData: String, old: RecordDTO): RecordDTO {
            log(old.recTooltip, newData)
            return old.copy(recTooltip = newData)
        }
    }

    data object EngineDataType : RecordDataType<String>(code = 16) {

        override fun read(dataArr: ByteArray, offset: Int, length: Int): String =
            ByteArrayToTextMapper(dataArr, offset, length, Charsets.ISO_8859_1)

        override fun map(newData: String, old: RecordDTO): RecordDTO {
            log(old.engine, newData)
            return old.copy(engine = newData)
        }
    }

    data object StorageDataType : NumberDataType(code = 17) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            val freeSpace = newData.mb
            log(old.freeSpace.mb, freeSpace.mb)
            return old.copy(freeSpace = freeSpace)
        }
    }

    data object StreamTypeDataType : NumberDataType(code = 18) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            val streamType = StreamType[newData.toInt()]
            log(old.streamType, streamType)
            return old.copy(streamType = streamType)
        }
    }

    data object RecordOrStreamDataType : NumberDataType(code = 19) {

        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            val stream = 3L // the value of the field while the PC streams rather than records
            val isStream = newData == stream
            log(old.isStream, isStream)
            return old.copy(isStream = isStream)
        }
    }

    data object WebCamDataType : NumberDataType(code = 20) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            val isWebCam = newData != 0L
            log(old.isWebCam, isWebCam)
            return old.copy(isWebCam = isWebCam)
        }
    }

    data object MicDataType : NumberDataType(code = 21) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            val isMic = newData != 0L
            log(old.isMic, isMic)
            return old.copy(isMic = isMic)
        }
    }

    data object GameActiveDataType : NumberDataType(code = 23) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            val gameActive = newData != 0L
            log(old.gameActive, gameActive)
            return old.copy(gameActive = gameActive)
        }
    }

    data object WebCamTypeDataType : NumberDataType(code = 24) {
        override fun map(newData: Long, old: RecordDTO): RecordDTO {
            val webCamType = newData.toInt()
            log(old.webCamType, webCamType)
            return old.copy(webCamType = newData.toInt())
        }
    }

    /**
     * A chunk of a frame of a web camera, assembled into [RecordDTO.webCamDataList] as the release
     * 1.0.4 does: the chunks of one frame fill its assembling bytes, and the frame is copied to the
     * shown bytes once its chunks are in. No screen shows the frames before version 1.2.0; a chunk
     * outside its packet or its frame, or of a frame with no size, is skipped.
     */
    data object WebDataDataType : RecordDataType<WebCamChunk?>(code = 25) {

        override fun read(dataArr: ByteArray, offset: Int, length: Int): WebCamChunk? {
            val frameIdField = 0
            val chunkIndexField = 1
            val cameraIndexField = 2
            val widthField = 3
            val heightField = 4
            val chunkPixelsField = 5
            val pixelsLengthField = 6
            val headerSize = 7 * Int.SIZE_BYTES // seven numbers of four bytes

            if (length < headerSize) return null

            val chunk = WebCamChunk(
                frameId = readInt(dataArr, offset, frameIdField),
                chunkIndex = readInt(dataArr, offset, chunkIndexField),
                cameraIndex = readInt(dataArr, offset, cameraIndexField),
                width = readInt(dataArr, offset, widthField),
                height = readInt(dataArr, offset, heightField),
                chunkPixels = readInt(dataArr, offset, chunkPixelsField),
                pixelsLength = readInt(dataArr, offset, pixelsLengthField),
                dataArr = dataArr,
                pixelsOffset = offset + headerSize,
            )
            val isInPacket = chunk.pixelsLength in 0..(length - headerSize)
            val hasSize = chunk.width > 0 && chunk.height > 0 && chunk.chunkPixels > 0

            return if (isInPacket && hasSize && chunk.chunkIndex >= 0) chunk else null
        }

        override fun map(newData: WebCamChunk?, old: RecordDTO): RecordDTO {
            val chunk = newData ?: return old
            val cameraIndex = chunk.cameraIndex

            if (cameraIndex !in 0 until old.webCamType) return old

            val webCam = old.webCamDataList.getOrNull(cameraIndex) ?: return old
            val isFrameReadyInPacket = old.isWebCamFrameReady
            val assembled = assemble(chunk, webCam, isFrameReadyInPacket) ?: return old
            val webCamList = old.webCamDataList.toMutableList()

            webCamList[cameraIndex] = assembled.copy(a = chunk.frameId)

            val isFrameReady = isFrameReadyInPacket || isFrameCompleted(chunk, assembled)

            return old.copy(webCamDataList = webCamList, isWebCamFrameReady = isFrameReady)
        }

        /**
         * Copies the chunk into the assembling bytes of its camera and, once the frame is complete
         * or a frame was completed earlier in the packet, the assembling bytes into the shown ones.
         *
         * @return the camera with the chunk applied, or null when the chunk is of an older frame
         * or falls outside the frame.
         */
        private fun assemble(
            chunk: WebCamChunk,
            webCam: WebCamData,
            isFrameReadyInPacket: Boolean,
        ): WebCamData? {
            val bytesPerPixel = 2
            val frameSize = chunk.width.toLong() * chunk.height * bytesPerPixel

            if (frameSize > Int.MAX_VALUE) return null

            var camera = webCam
            var assemblingArr = camera.f
            var shownArr = camera.g

            if (assemblingArr == null || shownArr == null || shownArr.size < frameSize) {
                assemblingArr = ByteArray(frameSize.toInt())
                shownArr = ByteArray(frameSize.toInt())
                camera = camera.copy(a = chunk.frameId, f = assemblingArr, g = shownArr)
            }

            if (camera.a == 0) camera = camera.copy(a = chunk.frameId)

            val destination = chunk.chunkIndex.toLong() * bytesPerPixel * chunk.chunkPixels
            val isInFrame = destination + chunk.pixelsLength <= assemblingArr.size

            if (camera.a > chunk.frameId || !isInFrame) return null

            chunk.dataArr.copyInto(
                destination = assemblingArr,
                destinationOffset = destination.toInt(),
                startIndex = chunk.pixelsOffset,
                endIndex = chunk.pixelsOffset + chunk.pixelsLength,
            )
            camera = camera.copy(b = chunk.cameraIndex, c = chunk.width, d = chunk.height)

            if (camera.a != chunk.frameId) return camera.copy(e = 0)

            camera = camera.copy(e = camera.e + 1)

            if (isFrameReadyInPacket || isFrameCompleted(chunk, camera)) {
                assemblingArr.copyInto(shownArr)
            }

            return camera
        }

        /** Whether the chunks the camera counted complete the frame of the chunk */
        private fun isFrameCompleted(chunk: WebCamChunk, camera: WebCamData): Boolean =
            camera.a == chunk.frameId &&
                camera.e >= chunk.width * chunk.height / chunk.chunkPixels - 1

        private fun readInt(dataArr: ByteArray, offset: Int, index: Int): Int =
            ByteArrayToNumberMapper(
                dataArr = dataArr,
                offset = offset + index * Int.SIZE_BYTES,
                length = Int.SIZE_BYTES,
            ).toInt()
    }
}

/**
 * A chunk of a frame of a web camera, as status field 25 carries it: seven numbers of four bytes,
 * then the RGB565 pixels.
 *
 * @property frameId the number of the frame the chunk belongs to.
 * @property chunkIndex the place of the chunk in its frame, counted in chunks.
 * @property cameraIndex the camera of the frame, from 0.
 * @property width the width of the frame in pixels.
 * @property height the height of the frame in pixels.
 * @property chunkPixels the pixels of a whole chunk.
 * @property pixelsLength the length of the pixel bytes of this chunk.
 * @property dataArr the bytes of the status packet.
 * @property pixelsOffset the index of the first pixel byte in [dataArr].
 */
data class WebCamChunk(
    val frameId: Int,
    val chunkIndex: Int,
    val cameraIndex: Int,
    val width: Int,
    val height: Int,
    val chunkPixels: Int,
    val pixelsLength: Int,
    val dataArr: ByteArray,
    val pixelsOffset: Int,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        return other is WebCamChunk &&
            frameId == other.frameId &&
            chunkIndex == other.chunkIndex &&
            cameraIndex == other.cameraIndex &&
            width == other.width &&
            height == other.height &&
            chunkPixels == other.chunkPixels &&
            pixelsLength == other.pixelsLength &&
            dataArr.contentEquals(other.dataArr) &&
            pixelsOffset == other.pixelsOffset
    }

    override fun hashCode(): Int {
        var result = frameId

        result = 31 * result + chunkIndex
        result = 31 * result + cameraIndex
        result = 31 * result + width
        result = 31 * result + height
        result = 31 * result + chunkPixels
        result = 31 * result + pixelsLength
        result = 31 * result + dataArr.contentHashCode()
        result = 31 * result + pixelsOffset

        return result
    }
}
