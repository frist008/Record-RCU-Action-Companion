package ua.frist008.action.record.features.settings

/** What the user does on the settings screen; a preview takes the empty defaults */
interface SettingsActions {

    fun onMinFreeSpaceChange(isIncrease: Boolean) {}

    fun onLowSpaceSoundChange(isChecked: Boolean) {}

    fun onRecordButtonSoundChange(isChecked: Boolean) {}

    fun onPauseButtonSoundChange(isChecked: Boolean) {}

    fun onStopButtonSoundChange(isChecked: Boolean) {}

    fun onDurationSoundChange(isChecked: Boolean) {}

    fun onDurationSoundAfterChange(isIncrease: Boolean) {}

    fun onDimAfterChange(isIncrease: Boolean) {}

    fun onDimToChange(isIncrease: Boolean) {}

    fun onFirstTouchChange(isChecked: Boolean) {}

    fun onLanguageClick() {}

    fun onAboutClick() {}

    fun onAdsPrivacyClick() {}
}
