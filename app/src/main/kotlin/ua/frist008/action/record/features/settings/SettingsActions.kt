package ua.frist008.action.record.features.settings

/** What the user does on the settings screen; a preview takes the empty defaults */
interface SettingsActions {

    fun onMinFreeSpaceChange(isIncrease: Boolean) = Unit

    fun onLowSpaceSoundChange(isChecked: Boolean) = Unit

    fun onRecordButtonSoundChange(isChecked: Boolean) = Unit

    fun onPauseButtonSoundChange(isChecked: Boolean) = Unit

    fun onStopButtonSoundChange(isChecked: Boolean) = Unit

    fun onDurationSoundChange(isChecked: Boolean) = Unit

    fun onDurationSoundAfterChange(isIncrease: Boolean) = Unit

    fun onDimAfterChange(isIncrease: Boolean) = Unit

    fun onDimToChange(isIncrease: Boolean) = Unit

    fun onFirstTouchChange(isChecked: Boolean) = Unit

    fun onLanguageClick() = Unit

    fun onAboutClick() = Unit

    fun onAdsPrivacyClick() = Unit
}