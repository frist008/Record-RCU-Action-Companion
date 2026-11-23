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
        val minFreeSpaceStepGb = 1
        val minFreeSpaceMinGb = 1
        val minFreeSpaceMaxGb = 100

        update { settings ->
            val gb = step(settings.minFreeSpace.gb.toInt(), minFreeSpaceStepGb, isIncrease)
                .coerceIn(minFreeSpaceMinGb, minFreeSpaceMaxGb)

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
        val durationSoundStep = 5.minutes
        val durationSoundMin = 5.minutes
        val durationSoundMax = 600.minutes

        update { settings ->
            val after = step(settings.durationSoundAfter, durationSoundStep, isIncrease)
                .coerceIn(durationSoundMin, durationSoundMax)

            settings.copy(durationSoundAfter = after)
        }
    }

    override fun onDimAfterChange(isIncrease: Boolean) {
        val dimAfterStep = 1.minutes
        val dimAfterMin = 1.minutes
        val dimAfterMax = 60.minutes

        update { settings ->
            val after = step(settings.dimAfter, dimAfterStep, isIncrease)
                .coerceIn(dimAfterMin, dimAfterMax)

            settings.copy(dimAfter = after)
        }
    }

    override fun onDimToChange(isIncrease: Boolean) {
        val dimToStepPercent = 10
        val dimToMinPercent = 0
        val dimToMaxPercent = 90

        update { settings ->
            val percent = step(settings.dimToPercent, dimToStepPercent, isIncrease)
                .coerceIn(dimToMinPercent, dimToMaxPercent)

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
}
