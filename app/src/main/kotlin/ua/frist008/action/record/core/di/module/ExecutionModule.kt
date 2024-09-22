package ua.frist008.action.record.core.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import ua.frist008.action.record.core.di.qualifier.concurrent.Computation
import ua.frist008.action.record.core.di.qualifier.concurrent.IO
import ua.frist008.action.record.core.di.qualifier.concurrent.Main

@Module
@InstallIn(SingletonComponent::class)
class ExecutionModule {

    @Provides @Main
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides @Computation
    fun provideComputationDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides @IO
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}
