package ua.frist008.action.record.di.module.ui

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import ua.frist008.action.record.ui.navigation.Navigator

@Module
@InstallIn(ActivityRetainedComponent::class)
class NavigationModule {

    @Provides @ActivityRetainedScoped
    fun provideNavigator(): Navigator = Navigator()
}
