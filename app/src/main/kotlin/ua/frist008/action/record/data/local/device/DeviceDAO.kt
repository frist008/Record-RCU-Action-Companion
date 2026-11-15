package ua.frist008.action.record.data.local.device

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/** Keeps the PC the phone connected to last in DataStore */
@Singleton class DeviceDAO @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {

    /**
     * Remembers the PC as the last one the phone connected to.
     *
     * @param dbo the PC to remember.
     * @throws IOException when DataStore cannot write its file.
     */
    suspend fun insert(dbo: DeviceDBO) {
        dataStore.edit { preferences ->
            preferences[IP_KEY] = dbo.ip
            preferences[PORT_KEY] = dbo.port

            if (dbo.name == null) {
                preferences.remove(NAME_KEY)
            } else {
                preferences[NAME_KEY] = dbo.name
            }
        }
    }

    /**
     * Reads the PC the phone connected to last.
     *
     * @return the PC, or null when the phone has not connected to one yet or its file is unreadable.
     */
    suspend fun get(): DeviceDBO? {
        val preferences = try {
            dataStore.data.firstOrNull()
        } catch (e: IOException) {
            Timber.w(e)
            null
        }
        val ip = preferences?.get(IP_KEY)
        val port = preferences?.get(PORT_KEY)

        return if (ip == null || port == null) {
            null
        } else {
            DeviceDBO(name = preferences?.get(NAME_KEY), ip = ip, port = port)
        }
    }

    companion object {

        private val NAME_KEY = stringPreferencesKey("last_device_name")
        private val IP_KEY = longPreferencesKey("last_device_ip")
        private val PORT_KEY = intPreferencesKey("last_device_port")
    }
}