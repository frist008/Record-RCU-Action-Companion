package ua.frist008.action.record.features.settings

import kotlinx.coroutines.flow.Flow
import ua.frist008.action.record.features.settings.entity.SettingsDomainEntity

interface SettingsRepository {

    /** The settings, again on every change; the defaults until the user changes one */
    val settingsFlow: Flow<SettingsDomainEntity>

    /**
     * Changes the settings in one write, so two changes in a row never lose one.
     *
     * @param transform makes the new settings from the saved ones.
     */
    suspend fun update(transform: (SettingsDomainEntity) -> SettingsDomainEntity)

    /**
     * Reads the language the user picked for the app in Android, Android 13 and newer only.
     *
     * @return its name in itself, or null when the app follows the language of the system.
     */
    fun getAppLanguage(): String?

    /**
     * Tells whether the law of the user's country asks the app to keep their consent to ads open to
     * change, as Google's consent form found it on the last start.
     *
     * @return true when the settings have to lead to that form.
     */
    fun isAdsPrivacyRequired(): Boolean
}
