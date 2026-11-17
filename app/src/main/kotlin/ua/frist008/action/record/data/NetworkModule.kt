package ua.frist008.action.record.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.network.selector.SelectorManager
import kotlinx.coroutines.CoroutineDispatcher
import ua.frist008.action.record.core.di.qualifier.concurrent.IO
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    /** One selector for every socket of the process, never closed while a socket may live */
    @Provides @Singleton
    fun provideSelectorManager(@IO ioDispatcher: CoroutineDispatcher): SelectorManager =
        SelectorManager(ioDispatcher)
}
