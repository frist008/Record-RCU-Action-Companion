package ua.frist008.action.record.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.frist008.action.record.data.network.device.DeviceRadarRepositoryImpl
import ua.frist008.action.record.data.network.record.RecordRepositoryImpl
import ua.frist008.action.record.features.device.DeviceRadarRepository
import ua.frist008.action.record.features.record.RecordRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds @Singleton
    fun bindDeviceRadarRepository(repository: DeviceRadarRepositoryImpl): DeviceRadarRepository

    @Binds @Singleton
    fun bindRecordRepository(repository: RecordRepositoryImpl): RecordRepository
}
