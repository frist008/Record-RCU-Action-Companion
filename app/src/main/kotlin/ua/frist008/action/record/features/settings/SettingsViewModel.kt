package ua.frist008.action.record.features.settings

import android.os.Build
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.Dispatchers
import ua.frist008.action.record.BuildConfig
import ua.frist008.action.record.core.presentation.BaseViewModel
import ua.frist008.action.record.core.presentation.dependency.PresentationDependenciesDelegate
import ua.frist008.action.record.core.util.io.gb
import ua.frist008.action.record.features.NavCommand
import ua.frist008.action.record.features.settings.entity.SettingsDomainEntity
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@HiltViewModel class SettingsViewModel @Inject constructor(
    dependencies: PresentationDependenciesDelegate,
    private val settingsRepository: SettingsRepository,
) : BaseViewModel(dependencies),
    SettingsActions {

    private val lastSettings = atomic(SettingsDomainEntity())

    init {
        launch {
            settingsRepository.settingsFlow.collect { settings ->
                lastSettings.value = settings
                showSettings(settings)
            }
        }
    }

    /** Shows the language again: the user may have changed it in Android meanwhile */
    fun onResume() {
        launch {
            showSettings(lastSettings.value)
        }
    }

    override fun onMinFreeSpaceChange(isIncrease: Boolean) {
        update { settings ->
            val gb = step(settings.minFreeSpace.gb.toInt(), MIN_FREE_SPACE_STEP_GB, isIncrease)
                .coerceIn(MIN_FREE_SPACE_MIN_GB, MIN_FREE_SPACE_MAX_GB)

            settings.copy(minFreeSpace = gb.gb)
        }
    }

    override fun onLowSpaceSoundChange(isChecked: Boolean) {
        update { settings -> settings.copy(isLowSpaceSound = isChecked) }
    }

    override fun onRecordButtonSoundChange(isChecked: Boolean) {
        update { settings -> settings.copy(isRecordButtonSound = isChecked) }
    }

    override fun onPauseButtonSoundChange(isChecked: Boolean) {
        update { settings -> settings.copy(isPauseButtonSound = isChecked) }
    }

    override fun onStopButtonSoundChange(isChecked: Boolean) {
        update { settings -> settings.copy(isStopButtonSound = isChecked) }
    }

    override fun onDurationSoundChange(isChecked: Boolean) {
        update { settings -> settings.copy(isDurationSound = isChecked) }
    }

    override fun onDurationSoundAfterChange(isIncrease: Boolean) {
        update { settings ->
            val after = step(settings.durationSoundAfter, DURATION_SOUND_STEP, isIncrease)
                .coerceIn(DURATION_SOUND_MIN, DURATION_SOUND_MAX)

            settings.copy(durationSoundAfter = after)
        }
    }

    override fun onDimAfterChange(isIncrease: Boolean) {
        update { settings ->
            val after = step(settings.dimAfter, DIM_AFTER_STEP, isIncrease)
                .coerceIn(DIM_AFTER_MIN, DIM_AFTER_MAX)

            settings.copy(dimAfter = after)
        }
    }

    override fun onDimToChange(isIncrease: Boolean) {
        update { settings ->
            val percent = step(settings.dimToPercent, DIM_TO_STEP_PERCENT, isIncrease)
                .coerceIn(DIM_TO_MIN_PERCENT, DIM_TO_MAX_PERCENT)

            settings.copy(dimToPercent = percent)
        }
    }

    override fun onFirstTouchChange(isChecked: Boolean) {
        update { settings -> settings.copy(isFirstTouchOnlyWakes = isChecked) }
    }

    override fun onLanguageClick() {
        launch(Dispatchers.Main.immediate) {
            navigator.emit(NavCommand.AppLanguageSettings)
        }
    }

    override fun onAboutClick() {
        launch(Dispatchers.Main.immediate) {
            navigator.emit(NavCommand.AboutScreen)
        }
    }

    override fun onAdsPrivacyClick() {
        launch(Dispatchers.Main.immediate) {
            navigator.emit(NavCommand.AdsPrivacyOptions)
        }
    }

    private suspend fun showSettings(settings: SettingsDomainEntity) {
        val state = settings.toUI(
            isLanguageVisible = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU,
            languageName = settingsRepository.getAppLanguage(),
            versionName = BuildConfig.VERSION_NAME,
            isAdsPrivacyVisible = settingsRepository.isAdsPrivacyRequired(),
        )

        mutableState.emit(state)
    }

    private fun update(transform: (SettingsDomainEntity) -> SettingsDomainEntity) {
        launch {
            settingsRepository.update(transform)
        }
    }

    private fun step(value: Int, step: Int, isIncrease: Boolean): Int =
        if (isIncrease) value + step else value - step

    private fun step(value: Duration, step: Duration, isIncrease: Boolean): Duration =
        if (isIncrease) value + step else value - step

    companion object {

        private const val MIN_FREE_SPACE_STEP_GB = 1
        private const val MIN_FREE_SPACE_MIN_GB = 1
        private const val MIN_FREE_SPACE_MAX_GB = 100

        private val DURATION_SOUND_STEP = 5.minutes
        private val DURATION_SOUND_MIN = 5.minutes
        private val DURATION_SOUND_MAX = 600.minutes

        private val DIM_AFTER_STEP = 1.minutes
        private val DIM_AFTER_MIN = 1.minutes
        private val DIM_AFTER_MAX = 60.minutes

        private const val DIM_TO_STEP_PERCENT = 10
        private const val DIM_TO_MIN_PERCENT = 0
        private const val DIM_TO_MAX_PERCENT = 90
    }
}