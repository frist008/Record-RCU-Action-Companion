package ua.frist008.action.record.di.module.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.frist008.action.record.data.infrastructure.repository.DeviceRadarRepositoryImpl
import ua.frist008.action.record.data.repository.DeviceRadarRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds @Singleton
    fun bindDeviceRadarRepository(repository: DeviceRadarRepositoryImpl): DeviceRadarRepository
}
