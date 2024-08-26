package ua.frist008.action.record.di.module.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.frist008.action.record.data.infrastructure.source.network.DeviceRadarNetworkSourceImpl
import ua.frist008.action.record.data.infrastructure.source.network.RecordNetworkSourceImpl
import ua.frist008.action.record.data.source.DeviceRadarNetworkSource
import ua.frist008.action.record.data.source.RecordNetworkSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface SourceModule {

    @Binds @Singleton
    fun bindDeviceRadarNetworkSource(source: DeviceRadarNetworkSourceImpl): DeviceRadarNetworkSource

    @Binds @Singleton
    fun bindRecordNetworkSource(source: RecordNetworkSourceImpl): RecordNetworkSource
}
