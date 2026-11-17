package ua.frist008.action.record.features.premium

import android.app.Activity
import dagger.hilt.android.lifecycle.HiltViewModel
import ua.frist008.action.record.R
import ua.frist008.action.record.core.presentation.BaseViewModel
import ua.frist008.action.record.core.presentation.dependency.PresentationDependenciesDelegate
import ua.frist008.action.record.features.NavCommand
import ua.frist008.action.record.features.premium.entity.PremiumSuccessState
import javax.inject.Inject

/**
 * The offer of Premium: shows the price Google Play sells it for and goes back to the record screen
 * once it is bought, here or before.
 */
@HiltViewModel class PremiumViewModel @Inject constructor(
    dependencies: PresentationDependenciesDelegate,
    private val premiumRepository: PremiumRepository,
) : BaseViewModel(dependencies) {

    init {
        launch {
            val price = premiumRepository.getPrice()

            if (price == null) {
                toastFlow.emit(R.string.error_unknown_error)
                openRecord()
            } else {
                mutableState.emit(PremiumSuccessState(price = price))
            }
        }

        launch {
            premiumRepository.isPremiumFlow.collect { isPremium ->
                if (isPremium == true) openRecord()
            }
        }
    }

    /** Asks Google Play again: a purchase may have been made or paid while the app was away */
    fun onResume() {
        launch {
            premiumRepository.refresh()
        }
    }

    /** @param activity the screen the window of Google Play opens over */
    fun onBuyClick(activity: Activity) {
        launch {
            if (!premiumRepository.buy(activity)) toastFlow.emit(R.string.error_unknown_error)
        }
    }

    fun onRestoreClick() {
        launch {
            premiumRepository.refresh()
        }
    }

    /** Goes back to the record screen, however often it is asked */
    private suspend fun openRecord() {
        navigator.emit(NavCommand.BackCommand(backToScreen = NavCommand.RecordScreen))
    }
}
