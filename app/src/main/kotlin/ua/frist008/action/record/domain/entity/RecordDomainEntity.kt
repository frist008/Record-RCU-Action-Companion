package ua.frist008.action.record.domain.entity

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import ua.frist008.action.record.R
import ua.frist008.action.record.data.infrastructure.entity.dto.WebCamData
import ua.frist008.action.record.data.infrastructure.entity.dto.type.RecordModeType
import ua.frist008.action.record.data.infrastructure.entity.dto.type.StreamType
import ua.frist008.action.record.ui.entity.record.EngineState
import ua.frist008.action.record.ui.entity.record.ErrorType
import ua.frist008.action.record.ui.entity.record.LiveState
import ua.frist008.action.record.ui.entity.record.RecordButtonsState
import ua.frist008.action.record.ui.entity.record.RecordSuccessState
import ua.frist008.action.record.ui.entity.record.StorageState
import ua.frist008.action.record.util.date.DateUtils
import ua.frist008.action.record.util.extension.common.round
import ua.frist008.action.record.util.io.Space
import ua.frist008.action.record.util.io.gb
import kotlin.time.Duration

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
                timeState = mutableStateOf(mapDuration(duration)),
                // TODO maxDuration = maxDuration
                // TODO recTooltip = recTooltip
                engine = mapEngine(engine),
                storage = mapStorage(null, freeSpace),
                live = mapLiveState(streamType, isStream),
                isWebCam = isWebCam,
                isMic = isMic,
                gameActive = gameActive,
                // TODO webCamType = webCamType,
                // TODO webCamDataList = webCamDataList,
            )
        } else {
            recordSuccessState.fpsState.value = fps.toString()
            recordSuccessState.timeState.value = mapDuration(duration)
            recordSuccessState.copy(
                buttonsData = mapRecordType(recordModeType, isStream),
                fps = fps,
                maxFps = maxFps,
                // TODO maxDuration = maxDuration
                // TODO recTooltip = recTooltip
                engine = mapEngine(engine),
                storage = mapStorage(recordSuccessState.storage.freeSpaceState, freeSpace),
                live = mapLiveState(streamType, isStream),
                isWebCam = isWebCam,
                isMic = isMic,
                gameActive = gameActive,
                // TODO webCamType = webCamType,
                // TODO webCamDataList = webCamDataList,
            )
        }
    }

    private fun mapLiveState(streamType: StreamType, isStream: Boolean) =
        LiveState(isOnline = streamType != StreamType.OFF, isLive = isStream)

    private fun mapStorage(freeSpaceState: MutableState<Float>?, space: Space): StorageState =
        if (space < 1.gb) {
            val freeMB = space.mb.toInt().toFloat()
            freeSpaceState?.value = freeMB

            StorageState(
                freeSpaceState = freeSpaceState ?: mutableFloatStateOf(freeMB),
                pattern = R.string.record_header_storage_mb_pattern,
                errorType = ErrorType.ERROR,
            )
        } else {
            val freeGB = space.gb.round(1).toFloat()
            freeSpaceState?.value = freeGB

            StorageState(
                freeSpaceState = freeSpaceState ?: mutableFloatStateOf(freeGB),
                pattern = R.string.record_header_storage_gb_pattern,
                errorType = if (freeGB < 10f) ErrorType.WARNING else ErrorType.DEFAULT,
            )
        }

    private fun mapDuration(duration: Duration) =
        DateUtils.formatTime(DateUtils.fromDuration(duration))

    private fun mapEngine(engine: String) =
        // TODO
        EngineState(name = engine, errorType = ErrorType.DEFAULT)

    private fun mapRecordType(
        recordModeType: RecordModeType,
        isStream: Boolean,
    ): RecordButtonsState {
        val isRecording = recordModeType == RecordModeType.RECORD
        return RecordButtonsState(
            isRecordingVisible = isRecording && !isStream,
            isRecording = isRecording,
            isStopEnabled = isRecording || recordModeType == RecordModeType.PAUSE,
        )
    }
}
