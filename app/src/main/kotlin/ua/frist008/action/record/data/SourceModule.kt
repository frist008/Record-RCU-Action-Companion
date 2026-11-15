package ua.frist008.action.record.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.frist008.action.record.data.network.device.DeviceRadarNetworkSource
import ua.frist008.action.record.data.network.device.DeviceRadarNetworkSourceImpl
import ua.frist008.action.record.data.network.premium.PremiumNetworkSource
import ua.frist008.action.record.data.network.premium.PremiumNetworkSourceImpl
import ua.frist008.action.record.data.network.record.RecordNetworkSource
import ua.frist008.action.record.data.network.record.RecordNetworkSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface SourceModule {

    @Binds @Singleton
    fun bindDeviceRadarNetworkSource(source: DeviceRadarNetworkSourceImpl): DeviceRadarNetworkSource

    @Binds @Singleton
    fun bindRecordNetworkSource(source: RecordNetworkSourceImpl): RecordNetworkSource

    @Binds @Singleton
    fun bindPremiumNetworkSource(source: PremiumNetworkSourceImpl): PremiumNetworkSource
}
