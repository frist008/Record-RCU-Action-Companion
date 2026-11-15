package ua.frist008.action.record.data.local.settings

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import ua.frist008.action.record.features.settings.SettingsRepository
import ua.frist008.action.record.features.settings.entity.SettingsDomainEntity
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingsDAO: SettingsDAO,
    @param:ApplicationContext private val context: Context,
) : SettingsRepository {

    override val settingsFlow: Flow<SettingsDomainEntity> get() = settingsDAO.settingsFlow

    override suspend fun update(transform: (SettingsDomainEntity) -> SettingsDomainEntity) {
        settingsDAO.update(transform)
    }

    override fun getAppLanguage(): String? {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return null

        val locale = context.getSystemService(LocaleManager::class.java)
            ?.applicationLocales
            ?.get(0)
            ?: return null

        return locale.getDisplayName(locale).replaceFirstChar { it.titlecase(locale) }
    }
}