package ua.frist008.action.record.features.settings

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import ua.frist008.action.record.core.presentation.BaseViewModel
import ua.frist008.action.record.core.presentation.dependency.PresentationDependenciesDelegate
import ua.frist008.action.record.features.NavCommand
import javax.inject.Inject

/** The screen «About»: its links open the privacy policy and the open-source licenses */
@HiltViewModel class AboutViewModel @Inject constructor(
    dependencies: PresentationDependenciesDelegate,
) : BaseViewModel(dependencies) {

    /**
     * Opens the privacy policy of the app.
     *
     * @param url the address of the policy, a string resource of the screen.
     */
    fun onPrivacyPolicyClick(url: String) {
        launch(Dispatchers.Main.immediate) {
            navigator.emit(NavCommand.Link(url))
        }
    }

    fun onLicensesClick() {
        launch(Dispatchers.Main.immediate) {
            navigator.emit(NavCommand.OpenSourceLicenses)
        }
    }
}
