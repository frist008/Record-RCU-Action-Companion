package ua.frist008.action.record.di.module.presentation

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import ua.frist008.action.record.presentation.base.dependency.PresentationDependenciesDelegate
import ua.frist008.action.record.presentation.impl.dependency.PresentationDependencies

@Module
@InstallIn(ActivityRetainedComponent::class)
interface ViewModelModule {

    @Binds @ActivityRetainedScoped
    fun bindPresentationDependencies(dependencies: PresentationDependencies): PresentationDependenciesDelegate
}
