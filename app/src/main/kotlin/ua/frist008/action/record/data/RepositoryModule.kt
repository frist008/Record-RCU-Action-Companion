package ua.frist008.action.record.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.frist008.action.record.data.local.settings.SettingsRepositoryImpl
import ua.frist008.action.record.data.network.device.ConnectionRepositoryImpl
import ua.frist008.action.record.data.network.premium.PremiumRepositoryImpl
import ua.frist008.action.record.data.network.record.RecordRepositoryImpl
import ua.frist008.action.record.features.connection.ConnectionRepository
import ua.frist008.action.record.features.premium.PremiumRepository
import ua.frist008.action.record.features.record.RecordRepository
import ua.frist008.action.record.features.settings.SettingsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindConnectionRepository(repository: ConnectionRepositoryImpl): ConnectionRepository

    @Binds
    fun bindRecordRepository(repository: RecordRepositoryImpl): RecordRepository

    @Binds
    fun bindSettingsRepository(repository: SettingsRepositoryImpl): SettingsRepository

    /** A singleton: it remembers the purchases it confirmed to Google Play */
    @Binds @Singleton
    fun bindPremiumRepository(repository: PremiumRepositoryImpl): PremiumRepository
}
