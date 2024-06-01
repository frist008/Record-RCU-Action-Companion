package ua.frist008.action.record.di.module.common

import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ContextModule {

    @Provides
    fun provideResources(@ApplicationContext context: Context): Resources = context.resources
}
