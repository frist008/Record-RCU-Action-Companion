package ua.frist008.action.record.features.settings.entity

import androidx.compose.runtime.Immutable
import ua.frist008.action.record.core.ui.UIState

/**
 * The settings as the screen shows them.
 *
 * @property minFreeSpaceGb the free space below which Storage turns red, in gigabytes.
 * @property isLowSpaceSound whether the low space signal is on.
 * @property isRecordButtonSound whether the record button sounds.
 * @property isPauseButtonSound whether the pause button sounds.
 * @property isStopButtonSound whether the stop button sounds.
 * @property isDurationSound whether the recording time signal is on.
 * @property durationSoundAfterMinutes the length of the recording of that signal, in minutes.
 * @property dimAfterMinutes the time without a touch before the record screen dims, in minutes.
 * @property dimToPercent the brightness of the dimmed screen, in percent.
 * @property isFirstTouchOnlyWakes whether the first touch of the dimmed screen only restores it.
 * @property isLanguageVisible whether Android lets the app pick its own language.
 * @property languageName the language the user picked for the app, or null for the system one.
 * @property versionName the version of the app.
 */
@Immutable data class SettingsSuccessState(
    val minFreeSpaceGb: Int,
    val isLowSpaceSound: Boolean,
    val isRecordButtonSound: Boolean,
    val isPauseButtonSound: Boolean,
    val isStopButtonSound: Boolean,
    val isDurationSound: Boolean,
    val durationSoundAfterMinutes: Int,
    val dimAfterMinutes: Int,
    val dimToPercent: Int,
    val isFirstTouchOnlyWakes: Boolean,
    val isLanguageVisible: Boolean,
    val languageName: String?,
    val versionName: String,
) : UIState.Success() {

    companion object {

        fun preview() = SettingsSuccessState(
            minFreeSpaceGb = 10,
            isLowSpaceSound = true,
            isRecordButtonSound = false,
            isPauseButtonSound = false,
            isStopButtonSound = false,
            isDurationSound = true,
            durationSoundAfterMinutes = 30,
            dimAfterMinutes = 10,
            dimToPercent = 10,
            isFirstTouchOnlyWakes = false,
            isLanguageVisible = true,
            languageName = null,
            versionName = "1.0.4",
        )
    }
}