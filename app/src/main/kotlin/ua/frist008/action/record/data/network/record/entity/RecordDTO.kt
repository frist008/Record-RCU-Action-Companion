package ua.frist008.action.record.data.network.record.entity

import ua.frist008.action.record.core.util.io.Space
import ua.frist008.action.record.core.util.io.bytes
import ua.frist008.action.record.features.record.entity.RecordDomainEntity
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class RecordDTO(
    val connected: Boolean,
    val recordModeType: RecordModeType = RecordModeType.STOP,
    val fps: Int = 0,
    val duration: Duration = 0.seconds,
    val maxDuration: Duration = 0.seconds,
    val recTooltip: String = "",
    val engine: String = "",
    val freeSpace: Space = 0.bytes,
    val streamType: StreamType = StreamType.OFF,
    val isStream: Boolean = false,
    val maxFps: Int = 0,
    val isWebCam: Boolean = false,
    val isMic: Boolean = false,
    val gameActive: Boolean = false,
    val webCamType: Int = 0,
    val webCamDataList: List<WebCamData> = listOf(
        WebCamData(),
        WebCamData(),
        WebCamData(),
        WebCamData(),
    ),
    /** Whether a web camera frame was completed in the packet being read, false at its start */
    val isWebCamFrameReady: Boolean = false,
) {

    fun toDomain() = RecordDomainEntity(
        connected = connected,
        recordModeType = recordModeType,
        fps = fps,
        duration = duration,
        maxDuration = maxDuration,
        recTooltip = recTooltip,
        engine = engine,
        freeSpace = freeSpace,
        streamType = streamType,
        isStream = isStream,
        maxFps = maxFps,
        isWebCam = isWebCam,
        isMic = isMic,
        gameActive = gameActive,
        webCamType = webCamType,
        webCamDataList = webCamDataList,
    )
}

data class WebCamData(
    val a: Int = 0,
    val b: Int = -1,
    val c: Int = 0,
    val d: Int = 0,
    val e: Int = 0,
    val f: ByteArray? = null,
    val g: ByteArray? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        return other is WebCamData &&
            a == other.a &&
            b == other.b &&
            c == other.c &&
            d == other.d &&
            e == other.e &&
            f.contentEquals(other.f) &&
            g.contentEquals(other.g)
    }

    override fun hashCode(): Int {
        var result = a

        result = 31 * result + b
        result = 31 * result + c
        result = 31 * result + d
        result = 31 * result + e
        result = 31 * result + (f?.contentHashCode() ?: 0)
        result = 31 * result + (g?.contentHashCode() ?: 0)

        return result
    }
}
