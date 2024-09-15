package ua.frist008.action.record.presentation.base.dependency

import kotlinx.coroutines.flow.StateFlow
import ua.frist008.action.record.ui.entity.base.UIState

interface StateOwner {

    val state: StateFlow<UIState>

    companion object {

        inline fun <reified R> StateOwner.state(): R = state.value as R
    }
}
