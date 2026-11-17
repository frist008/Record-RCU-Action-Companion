package ua.frist008.action.record.data.local.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import timber.log.Timber
import ua.frist008.action.record.core.util.io.gb
import ua.frist008.action.record.features.settings.entity.SettingsDomainEntity
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.minutes

/** Keeps the settings the user picked in DataStore */
@Singleton class SettingsDAO @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {

    /** The settings, again on every change; the defaults while DataStore cannot read its file */
    val settingsFlow: Flow<SettingsDomainEntity> = dataStore.data
        .catch { cause ->
            if (cause is IOException) {
                Timber.w(cause)
                emit(emptyPreferences())
            } else {
                throw cause
            }
        }
        .map(::read)
        .distinctUntilChanged()

    /**
     * Changes the settings in one write, so two changes in a row never lose one.
     *
     * @param transform makes the new settings from the saved ones.
     * @throws IOException when DataStore cannot write its file.
     */
    suspend fun update(transform: (SettingsDomainEntity) -> SettingsDomainEntity) {
        dataStore.edit { preferences ->
            val settings = transform(read(preferences))

            preferences[MIN_FREE_SPACE_GB_KEY] = settings.minFreeSpace.gb.toInt()
            preferences[LOW_SPACE_SOUND_KEY] = settings.isLowSpaceSound
            preferences[RECORD_BUTTON_SOUND_KEY] = settings.isRecordButtonSound
            preferences[PAUSE_BUTTON_SOUND_KEY] = settings.isPauseButtonSound
            preferences[STOP_BUTTON_SOUND_KEY] = settings.isStopButtonSound
            preferences[DURATION_SOUND_KEY] = settings.isDurationSound
            preferences[DURATION_SOUND_MINUTES_KEY] =
                settings.durationSoundAfter.inWholeMinutes.toInt()
            preferences[DIM_AFTER_MINUTES_KEY] = settings.dimAfter.inWholeMinutes.toInt()
            preferences[DIM_TO_PERCENT_KEY] = settings.dimToPercent
            preferences[FIRST_TOUCH_ONLY_WAKES_KEY] = settings.isFirstTouchOnlyWakes
        }
    }

    private fun read(preferences: Preferences): SettingsDomainEntity {
        val default = SettingsDomainEntity()
        val minFreeSpaceGb = preferences[MIN_FREE_SPACE_GB_KEY]
        val durationSoundMinutes = preferences[DURATION_SOUND_MINUTES_KEY]
        val dimAfterMinutes = preferences[DIM_AFTER_MINUTES_KEY]

        return SettingsDomainEntity(
            minFreeSpace = minFreeSpaceGb?.gb ?: default.minFreeSpace,
            isLowSpaceSound = preferences[LOW_SPACE_SOUND_KEY] ?: default.isLowSpaceSound,
            isRecordButtonSound =
                preferences[RECORD_BUTTON_SOUND_KEY] ?: default.isRecordButtonSound,
            isPauseButtonSound = preferences[PAUSE_BUTTON_SOUND_KEY] ?: default.isPauseButtonSound,
            isStopButtonSound = preferences[STOP_BUTTON_SOUND_KEY] ?: default.isStopButtonSound,
            isDurationSound = preferences[DURATION_SOUND_KEY] ?: default.isDurationSound,
            durationSoundAfter = durationSoundMinutes?.minutes ?: default.durationSoundAfter,
            dimAfter = dimAfterMinutes?.minutes ?: default.dimAfter,
            dimToPercent = preferences[DIM_TO_PERCENT_KEY] ?: default.dimToPercent,
            isFirstTouchOnlyWakes =
                preferences[FIRST_TOUCH_ONLY_WAKES_KEY] ?: default.isFirstTouchOnlyWakes,
        )
    }

    companion object {

        private val MIN_FREE_SPACE_GB_KEY = intPreferencesKey("min_free_space_gb")
        private val LOW_SPACE_SOUND_KEY = booleanPreferencesKey("low_space_sound")
        private val RECORD_BUTTON_SOUND_KEY = booleanPreferencesKey("record_button_sound")
        private val PAUSE_BUTTON_SOUND_KEY = booleanPreferencesKey("pause_button_sound")
        private val STOP_BUTTON_SOUND_KEY = booleanPreferencesKey("stop_button_sound")
        private val DURATION_SOUND_KEY = booleanPreferencesKey("duration_sound")
        private val DURATION_SOUND_MINUTES_KEY = intPreferencesKey("duration_sound_minutes")
        private val DIM_AFTER_MINUTES_KEY = intPreferencesKey("dim_after_minutes")
        private val DIM_TO_PERCENT_KEY = intPreferencesKey("dim_to_percent")
        private val FIRST_TOUCH_ONLY_WAKES_KEY = booleanPreferencesKey("first_touch_only_wakes")
    }
}