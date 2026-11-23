package ua.frist008.action.record.features.record.entity

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.google.android.libraries.ads.mobile.sdk.banner.AdSize
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import ua.frist008.action.record.R
import ua.frist008.action.record.core.ui.UIState
import ua.frist008.action.record.core.ui.theme.color.AppColorScheme

@Immutable data class RecordSuccessState(
    val fps: Int,
    val maxFps: Int,
    val time: String,
    val engine: EngineState,
    val live: LiveState,
    val storage: StorageState,
    val buttonsData: RecordButtonsState,
    val isWebCam: Boolean,
    val isMic: Boolean,
    val gameActive: Boolean,
    val adSize: AdSize = AdSize.BANNER,
    val isSoundMuted: Boolean = false,
    val dimBrightness: Float? = null,
    val isFirstTouchOnlyWakes: Boolean = false,
    /** The banner and the way to remove it: shown once Google Play said Premium is not bought */
    val isAdsVisible: Boolean = false,
    /** The FPS of the last seconds the recording or the stream runs, one value a second */
    val fpsHistory: ImmutableList<Int> = persistentListOf(),
) : UIState.Success() {

    /**
     * The chart of [fpsHistory] under the FPS: shown while the recording or the stream runs, but
     * not while the stream is unavailable, as the design draws it; null while it is hidden.
     */
    val fpsChart = live.isLive.let { isLive ->
        if (buttonsData.isRecording && (!isLive || live.isOnline) && fpsHistory.size > 1) {
            FpsChartState(values = fpsHistory, maxFps = maxFps, isLive = isLive)
        } else {
            null
        }
    }

    /** The red camera beside the time: shown only while the recording runs */
    val isWebCamIndicatorVisible = isWebCam && buttonsData.isRecording

    /** The red microphone beside the time: shown only while the recording runs */
    val isMicIndicatorVisible = isMic && buttonsData.isRecording

    /** Whether a touch now only restores the brightness of the dimmed screen, pressing nothing */
    val isTouchOnlyWakes = dimBrightness != null && isFirstTouchOnlyWakes

    companion object {

        fun previewStop() = RecordSuccessState(
            fps = 60,
            maxFps = 100,
            time = "00:00:00",
            engine = EngineState.previewStopDefault(),
            live = LiveState.previewLiveOff(),
            storage = StorageState.previewDefault(),
            buttonsData = RecordButtonsState.previewStop(),
            gameActive = true,
            isWebCam = false,
            isMic = false,
            isAdsVisible = true,
        )

        fun previewRecording() = previewStop().copy(
            fps = 100,
            maxFps = 60,
            engine = EngineState.previewStopWarning(),
            live = LiveState.previewRecord(),
            storage = StorageState.previewWarning(),
            buttonsData = RecordButtonsState.previewRecording(),
            isWebCam = true,
            isMic = false,
            fpsHistory = previewFpsHistory(),
        )

        fun previewPaused() = previewStop().copy(
            fps = 6,
            maxFps = 30,
            engine = EngineState.previewStopError(),
            live = LiveState.previewRecord(),
            storage = StorageState.previewError(),
            buttonsData = RecordButtonsState.previewPaused(),
            isWebCam = false,
            isMic = true,
        )

        fun previewLive() = previewStop().copy(
            maxFps = 30,
            engine = EngineState.previewStopDefault(),
            live = LiveState.previewLiveOn(),
            storage = StorageState.previewDefault(),
            buttonsData = RecordButtonsState.previewStream(),
            isWebCam = true,
            isMic = true,
            fpsHistory = previewFpsHistory(),
        )

        /** A saw of the FPS: from 60 down by 3 a second, back to 60 every 7 seconds */
        private fun previewFpsHistory(): ImmutableList<Int> {
            val maxFps = 60
            val drop = 3
            val period = 7

            return List(FpsChartState.POINTS) { second -> maxFps - second % period * drop }
                .toImmutableList()
        }
    }
}

/**
 * The chart of the FPS under its number.
 *
 * @property values the FPS of the last seconds, one a second, the oldest first.
 * @property maxFps the FPS the top of the chart stands for.
 * @property isLive whether the stream runs, drawn green across the width, or the recording, red.
 */
@Immutable data class FpsChartState(
    val values: ImmutableList<Int>,
    val maxFps: Int,
    val isLive: Boolean,
) {

    companion object {

        /** The points of the chart, as many as Figma draws: the seconds it shows */
        const val POINTS = 37
    }
}

@Immutable data class EngineState(
    val name: String,
    val errorType: ErrorType,
) {

    companion object {
        fun previewStopDefault() = EngineState(
            name = "Windows",
            errorType = ErrorType.ERROR,
        )

        fun previewStopWarning() = EngineState(
            name = "Unknown",
            errorType = ErrorType.WARNING,
        )

        fun previewStopError() = EngineState(
            name = "Vulkan",
            errorType = ErrorType.DEFAULT,
        )
    }
}

@Immutable data class LiveState(
    val isOnline: Boolean,
    val isLive: Boolean,
) {

    companion object {
        fun previewRecord() = LiveState(isOnline = false, isLive = false)
        fun previewLiveOff() = LiveState(isOnline = false, isLive = true)
        fun previewLiveOn() = LiveState(isOnline = true, isLive = true)
    }
}

@Immutable data class StorageState(
    val freeSpace: Float,
    val timeRemaining: String,
    @get:StringRes val pattern: Int,
    val errorType: ErrorType,
    val freeSpaceWithTimestampList: ImmutableList<Pair<Long, Long>> = persistentListOf(),
) {

    companion object {
        fun previewDefault() = StorageState(
            freeSpace = 30.4f,
            timeRemaining = "",
            pattern = R.string.record_header_storage_gb_pattern,
            errorType = ErrorType.DEFAULT,
        )

        fun previewWarning() = StorageState(
            freeSpace = 9.72f,
            timeRemaining = "00:00:00",
            pattern = R.string.record_header_storage_gb_pattern,
            errorType = ErrorType.WARNING,
        )

        fun previewError() = StorageState(
            freeSpace = 30f,
            timeRemaining = "01:22:55",
            pattern = R.string.record_header_storage_mb_pattern,
            errorType = ErrorType.ERROR,
        )
    }
}

@Immutable data class RecordButtonsState(
    val isRecordingVisible: Boolean,
    val isStopVisible: Boolean,
    val isRecording: Boolean,
) {

    val isPaused = !isRecording && isStopVisible

    companion object {

        fun previewStop() = RecordButtonsState(
            isRecordingVisible = true,
            isRecording = false,
            isStopVisible = false,
        )

        fun previewRecording() = RecordButtonsState(
            isRecordingVisible = true,
            isRecording = true,
            isStopVisible = true,
        )

        fun previewPaused() = RecordButtonsState(
            isRecordingVisible = true,
            isRecording = false,
            isStopVisible = true,
        )

        fun previewStream() = RecordButtonsState(
            isRecordingVisible = false,
            isRecording = true,
            isStopVisible = true,
        )
    }
}

enum class ErrorType(val getColor: (AppColorScheme) -> Color) {
    DEFAULT({ it.onBackground }),
    WARNING({ it.warning }),
    ERROR({ it.error }),
}
