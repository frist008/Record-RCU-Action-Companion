package ua.frist008.action.record.features.record.entity

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
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
    val deviceId: Long,
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

    fun toUI(recordSuccessState: RecordSuccessState?): RecordSuccessState {
        // Investigate what is it
        // val fps = min(fps, maxFps)

        return if (recordSuccessState == null) {
            RecordSuccessState(
                buttonsData = mapRecordType(recordModeType, isStream),
                fps = fps,
                maxFps = maxFps,
                timeState = mutableStateOf(DateUtils.formatFullTime(duration)),
                // TODO maxDuration = maxDuration IMPORTANT
                // TODO recTooltip = recTooltip
                engine = mapEngine(engine),
                storage = mapStorage(
                    freeSpaceState = null,
                    timeRemainingState = null,
                    freeSpaceWithTimestampList = persistentListOf(),
                    freeSpace = freeSpace,
                ),
                live = mapLiveState(streamType, isStream),
                isWebCam = isWebCam, // TODO show cam IMPORTANT
                isMic = isMic, // TODO show mic IMPORTANT
                gameActive = gameActive,
                // TODO webCamType = webCamType,
                // TODO webCamDataList = webCamDataList,
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

            recordSuccessState.timeState.value = DateUtils.formatFullTime(duration)
            recordSuccessState.copy(
                buttonsData = buttonsData,
                fps = fps,
                maxFps = maxFps,
                // TODO maxDuration = maxDuration IMPORTANT
                // TODO recTooltip = recTooltip
                engine = mapEngine(engine),
                storage = mapStorage(
                    freeSpaceState = storage.freeSpaceState,
                    timeRemainingState = storage.timeRemainingState,
                    freeSpaceWithTimestampList = lastFreeSpaceWithTimestampList,
                    freeSpace = freeSpace,
                ),
                live = mapLiveState(streamType, isStream),
                isWebCam = isWebCam, // TODO show cam IMPORTANT
                isMic = isMic, // TODO show mic IMPORTANT
                gameActive = gameActive,
                // TODO webCamType = webCamType,
                // TODO webCamDataList = webCamDataList,
            )
        }
    }

    private fun mapLiveState(streamType: StreamType, isStream: Boolean) =
        LiveState(isOnline = streamType != StreamType.OFF, isLive = isStream)

    private fun mapStorage(
        freeSpaceState: MutableState<Float>?,
        timeRemainingState: MutableState<String>?,
        freeSpaceWithTimestampList: ImmutableList<Pair<Long, Long>>,
        freeSpace: Space,
    ): StorageState {
        val newFreeSpaceWithTimestampList =
            createFreeSpaceWithTimestampList(freeSpaceWithTimestampList, freeSpace)
        timeRemainingState?.value = calculateTimeRemaining(newFreeSpaceWithTimestampList, freeSpace)

        return if (freeSpace < 1.gb) {
            val freeMB = freeSpace.mb.toInt().toFloat()
            freeSpaceState?.value = freeMB

            StorageState(
                freeSpaceState = freeSpaceState ?: mutableFloatStateOf(freeMB),
                timeRemainingState = timeRemainingState ?: mutableStateOf(""),
                pattern = R.string.record_header_storage_mb_pattern,
                errorType = ErrorType.ERROR,
                freeSpaceWithTimestampList = newFreeSpaceWithTimestampList,
            )
        } else {
            val freeGB = freeSpace.gb.round(1).toFloat()
            freeSpaceState?.value = freeGB

            StorageState(
                freeSpaceState = freeSpaceState ?: mutableFloatStateOf(freeGB),
                timeRemainingState = timeRemainingState ?: mutableStateOf(""),
                pattern = R.string.record_header_storage_gb_pattern,
                errorType = if (freeGB < 10f) ErrorType.WARNING else ErrorType.DEFAULT,
                freeSpaceWithTimestampList = newFreeSpaceWithTimestampList,
            )
        }
    }

    private fun calculateTimeRemaining(
        lastFreeSpaceWithTimestamp: ImmutableList<Pair<Long, Long>>,
        freeSpace: Space,
    ): String {
        if (lastFreeSpaceWithTimestamp.size < 5) return ""

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
        if (freeSpace.bytes == 0L || freeSpace.bytes == list.lastOrNull()?.first) return list

        val newValue = freeSpace.bytes to Clock.System.now().toEpochMilliseconds()
        val trimmedList = (if (list.size > 15) list.subList(1, list.size) else list)

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
