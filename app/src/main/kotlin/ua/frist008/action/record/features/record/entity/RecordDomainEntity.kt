package ua.frist008.action.record.features.record.entity

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import ua.frist008.action.record.R
import ua.frist008.action.record.core.util.common.round
import ua.frist008.action.record.core.util.date.DateUtils
import ua.frist008.action.record.core.util.io.Space
import ua.frist008.action.record.core.util.io.gb
import ua.frist008.action.record.data.network.record.entity.RecordModeType
import ua.frist008.action.record.data.network.record.entity.StreamType
import ua.frist008.action.record.data.network.record.entity.WebCamData
import kotlin.math.abs
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime

data class RecordDomainEntity(
    val connected: Boolean,
    val recordModeType: RecordModeType,
    val fps: Int,
    val maxFps: Int,
    val duration: Duration,
    val maxDuration: Duration,
    val recTooltip: String,
    val engine: String,
    val freeSpace: Space,
    val streamType: StreamType,
    val isStream: Boolean,
    val isWebCam: Boolean,
    val isMic: Boolean,
    val gameActive: Boolean,
    val webCamType: Int,
    val webCamDataList: List<WebCamData>,
) {

    fun toUI(recordSuccessState: RecordSuccessState?, minFreeSpace: Space): RecordSuccessState {
        // Investigate what is it
        // val fps = min(fps, maxFps)

        return if (recordSuccessState == null) {
            val buttonsData = mapRecordType(recordModeType, isStream)

            RecordSuccessState(
                buttonsData = buttonsData,
                fps = fps,
                maxFps = maxFps,
                fpsHistory = mapFpsHistory(
                    fpsHistory = persistentListOf(),
                    fps = fps,
                    isRunning = buttonsData.isRecording,
                    isNewSecond = true,
                ),
                time = DateUtils.formatFullTime(duration),
                engine = mapEngine(engine),
                storage = mapStorage(
                    freeSpaceWithTimestampList = persistentListOf(),
                    freeSpace = freeSpace,
                    minFreeSpace = minFreeSpace,
                ),
                live = mapLiveState(streamType, isStream),
                isWebCam = isWebCam,
                isMic = isMic,
                gameActive = gameActive,
            )
        } else {
            val storage = recordSuccessState.storage
            val buttonsData = mapRecordType(recordModeType, isStream)
            val lastFreeSpaceWithTimestampList =
                if (buttonsData.isRecording) {
                    storage.freeSpaceWithTimestampList
                } else {
                    persistentListOf()
                }
            val time = DateUtils.formatFullTime(duration)

            recordSuccessState.copy(
                buttonsData = buttonsData,
                fps = fps,
                maxFps = maxFps,
                fpsHistory = mapFpsHistory(
                    fpsHistory = recordSuccessState.fpsHistory,
                    fps = fps,
                    isRunning = buttonsData.isRecording,
                    isNewSecond = time != recordSuccessState.time,
                ),
                time = time,
                engine = mapEngine(engine),
                storage = mapStorage(
                    freeSpaceWithTimestampList = lastFreeSpaceWithTimestampList,
                    freeSpace = freeSpace,
                    minFreeSpace = minFreeSpace,
                ),
                live = mapLiveState(streamType, isStream),
                isWebCam = isWebCam,
                isMic = isMic,
                gameActive = gameActive,
            )
        }
    }

    companion object {

        /**
         * Keeps the FPS of the last seconds the recording or the stream runs, one value a second,
         * for the chart under the FPS; a stop or a pause clears it.
         *
         * @param isNewSecond whether the time of the recording moved on since the last value.
         */
        private fun mapFpsHistory(
            fpsHistory: ImmutableList<Int>,
            fps: Int,
            isRunning: Boolean,
            isNewSecond: Boolean,
        ): ImmutableList<Int> = when {
            !isRunning -> persistentListOf()
            !isNewSecond -> fpsHistory
            else -> (fpsHistory.takeLast(FpsChartState.POINTS - 1) + fps).toImmutableList()
        }

        private fun mapLiveState(streamType: StreamType, isStream: Boolean) =
            LiveState(isOnline = streamType != StreamType.OFF, isLive = isStream)

        private fun mapStorage(
            freeSpaceWithTimestampList: ImmutableList<Pair<Long, Long>>,
            freeSpace: Space,
            minFreeSpace: Space,
        ): StorageState {
            val warningFreeSpaceGb = 10f // the free space in GB below which it is a warning
            val newFreeSpaceWithTimestampList =
                createFreeSpaceWithTimestampList(freeSpaceWithTimestampList, freeSpace)
            val timeRemaining = calculateTimeRemaining(newFreeSpaceWithTimestampList, freeSpace)

            return if (freeSpace < 1.gb) {
                StorageState(
                    freeSpace = freeSpace.mb.toInt().toFloat(),
                    timeRemaining = timeRemaining,
                    pattern = R.string.record_header_storage_mb_pattern,
                    errorType = ErrorType.ERROR,
                    freeSpaceWithTimestampList = newFreeSpaceWithTimestampList,
                )
            } else {
                val freeGB = freeSpace.gb.round(1).toFloat()

                StorageState(
                    freeSpace = freeGB,
                    timeRemaining = timeRemaining,
                    pattern = R.string.record_header_storage_gb_pattern,
                    errorType = when {
                        freeSpace < minFreeSpace -> ErrorType.ERROR
                        freeGB < warningFreeSpaceGb -> ErrorType.WARNING
                        else -> ErrorType.DEFAULT
                    },
                    freeSpaceWithTimestampList = newFreeSpaceWithTimestampList,
                )
            }
        }

        private fun calculateTimeRemaining(
            lastFreeSpaceWithTimestamp: ImmutableList<Pair<Long, Long>>,
            freeSpace: Space,
        ): String {
            // How many free-space samples the time left needs before it is shown
            val timeRemainingMinSamples = 5

            if (lastFreeSpaceWithTimestamp.size < timeRemainingMinSamples) return ""

            val (firstBytes, firstTimeMs) = lastFreeSpaceWithTimestamp.last()
            val (lastBytes, lastTimeMs) = lastFreeSpaceWithTimestamp.first()

            val differenceBetweenSpace = abs(firstBytes - lastBytes)
            val differenceBetweenTime = abs(firstTimeMs - lastTimeMs)

            val countTimesUntilSpaceEnded =
                if (differenceBetweenSpace > 0) freeSpace.bytes / differenceBetweenSpace else 0
            val timeUntilSpaceEndedMs = countTimesUntilSpaceEnded * differenceBetweenTime

            return DateUtils.formatFullTime(timeUntilSpaceEndedMs.milliseconds)
        }

        @OptIn(ExperimentalTime::class)
        private fun createFreeSpaceWithTimestampList(
            list: ImmutableList<Pair<Long, Long>>,
            freeSpace: Space,
        ): ImmutableList<Pair<Long, Long>> {
            val freeSpaceMaxSamples = 15 // the oldest free-space sample goes past it

            if (freeSpace.bytes == 0L || freeSpace.bytes == list.lastOrNull()?.first) return list

            val newValue = freeSpace.bytes to Clock.System.now().toEpochMilliseconds()
            val trimmedList =
                (if (list.size > freeSpaceMaxSamples) list.subList(1, list.size) else list)

            return (trimmedList.asSequence() + newValue).toImmutableList()
        }

        private fun mapEngine(engine: String): EngineState {
            val trimmedEngine = engine.trim()

            return when (trimmedEngine.lowercase()) {
                "aero" -> EngineState(name = "Desktop", errorType = ErrorType.ERROR)

                "dx9",
                "dx10",
                "dx11",
                "dx12",
                "opengl",
                "vulkan",
                -> EngineState(name = trimmedEngine, errorType = ErrorType.DEFAULT)

                else -> EngineState(name = trimmedEngine, errorType = ErrorType.DEFAULT)
            }
        }

        private fun mapRecordType(
            recordModeType: RecordModeType,
            isStream: Boolean,
        ): RecordButtonsState {
            val isRecording = recordModeType == RecordModeType.RECORD
            val isPaused = recordModeType == RecordModeType.PAUSE

            return RecordButtonsState(
                isRecordingVisible = !isRecording || !isStream,
                isRecording = isRecording,
                isStopVisible = isRecording || isPaused,
            )
        }
    }
}
