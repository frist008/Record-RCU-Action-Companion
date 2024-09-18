package ua.frist008.action.record.core.presentation

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import ua.frist008.action.record.core.presentation.dependency.PresentationDependencies
import ua.frist008.action.record.core.presentation.dependency.PresentationDependenciesDelegate

@Module
@InstallIn(ActivityRetainedComponent::class)
interface ViewModelModule {

    @Binds @ActivityRetainedScoped
    fun bindPresentationDependencies(dependencies: PresentationDependencies): PresentationDependenciesDelegate
}
