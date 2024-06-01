package ua.frist008.action.record.di.module.common

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import ua.frist008.action.record.di.qualifier.execute.Computation
import ua.frist008.action.record.di.qualifier.execute.IO
import ua.frist008.action.record.di.qualifier.execute.Main

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
