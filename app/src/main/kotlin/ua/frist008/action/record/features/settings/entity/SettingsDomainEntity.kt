package ua.frist008.action.record.features.settings.entity

import ua.frist008.action.record.core.util.io.Space
import ua.frist008.action.record.core.util.io.gb
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

/**
 * The settings the user picked; every default keeps the behaviour the app had before them.
 *
 * @property minFreeSpace the free space of the recording disk of the PC below which Storage turns red.
 * @property isLowSpaceSound whether a signal sounds once the free space falls below [minFreeSpace].
 * @property isRecordButtonSound whether the record button sounds when it is pressed.
 * @property isPauseButtonSound whether the pause button sounds when it is pressed.
 * @property isStopButtonSound whether the stop button sounds when it is pressed.
 * @property isDurationSound whether a signal sounds once the recording reaches [durationSoundAfter].
 * @property durationSoundAfter the length of the recording, by the timer of the recorder, of the signal.
 * @property dimAfter the time without a touch after which the record screen dims.
 * @property dimToPercent the brightness of the dimmed screen, in percent of the full one.
 * @property isFirstTouchOnlyWakes whether the first touch of the dimmed screen only restores the
 * brightness, pressing no button under the finger.
 */
data class SettingsDomainEntity(
    val minFreeSpace: Space = 1.gb,
    val isLowSpaceSound: Boolean = true,
    val isRecordButtonSound: Boolean = false,
    val isPauseButtonSound: Boolean = false,
    val isStopButtonSound: Boolean = false,
    val isDurationSound: Boolean = false,
    val durationSoundAfter: Duration = 30.minutes,
    val dimAfter: Duration = 10.minutes,
    val dimToPercent: Int = 10,
    val isFirstTouchOnlyWakes: Boolean = false,
) {

    fun toUI(isLanguageVisible: Boolean, languageName: String?, versionName: String) =
        SettingsSuccessState(
            minFreeSpaceGb = minFreeSpace.gb.toInt(),
            isLowSpaceSound = isLowSpaceSound,
            isRecordButtonSound = isRecordButtonSound,
            isPauseButtonSound = isPauseButtonSound,
            isStopButtonSound = isStopButtonSound,
            isDurationSound = isDurationSound,
            durationSoundAfterMinutes = durationSoundAfter.inWholeMinutes.toInt(),
            dimAfterMinutes = dimAfter.inWholeMinutes.toInt(),
            dimToPercent = dimToPercent,
            isFirstTouchOnlyWakes = isFirstTouchOnlyWakes,
            isLanguageVisible = isLanguageVisible,
            languageName = languageName,
            versionName = versionName,
        )
}