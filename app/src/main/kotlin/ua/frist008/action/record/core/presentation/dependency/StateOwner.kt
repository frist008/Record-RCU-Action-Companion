package ua.frist008.action.record.core.presentation.dependency

import kotlinx.coroutines.flow.StateFlow
import ua.frist008.action.record.core.ui.UIState

interface StateOwner {

    val state: StateFlow<UIState>

    companion object {

        inline fun <reified R> StateOwner.state(): R = state.value as R
    }
}
