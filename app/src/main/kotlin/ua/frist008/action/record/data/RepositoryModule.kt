package ua.frist008.action.record.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.frist008.action.record.data.network.device.ConnectionRepositoryImpl
import ua.frist008.action.record.data.network.record.RecordRepositoryImpl
import ua.frist008.action.record.features.connection.ConnectionRepository
import ua.frist008.action.record.features.record.RecordRepository

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindConnectionRepository(repository: ConnectionRepositoryImpl): ConnectionRepository

    @Binds
    fun bindRecordRepository(repository: RecordRepositoryImpl): RecordRepository
}
