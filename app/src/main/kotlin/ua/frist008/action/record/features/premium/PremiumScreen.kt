package ua.frist008.action.record.features.premium

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import ua.frist008.action.record.core.ui.UIState
import ua.frist008.action.record.core.ui.component.root.DefaultScaffold
import ua.frist008.action.record.core.ui.component.unsupportedUI
import ua.frist008.action.record.features.premium.entity.PremiumSuccessState

/** The design draws no title: the glow of the logo runs under the bare back arrow */
@Composable
fun PremiumScreen(viewModel: PremiumViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val activity = LocalActivity.current

    DefaultScaffold(title = "") { innerPadding ->
        when (val currentState = state) {
            is PremiumSuccessState -> PremiumSuccessScreen(
                state = currentState,
                innerPadding = innerPadding,
                onBuyClick = { activity?.let(viewModel::onBuyClick) },
                onRestoreClick = viewModel::onRestoreClick,
            )

            is UIState.Progress -> Unit
            else -> unsupportedUI()
        }
    }

    LifecycleResumeEffect(viewModel) {
        viewModel.onResume()
        onPauseOrDispose {}
    }
}