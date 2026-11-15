package ua.frist008.action.record.features.record

import android.view.Window
import android.view.WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import ua.frist008.action.record.core.ui.UIState
import ua.frist008.action.record.core.ui.component.unsupportedUI
import ua.frist008.action.record.features.record.entity.RecordSuccessState

@Composable
fun RecordScreen(viewModel: RecordViewModel = hiltViewModel()) {
    val view = LocalView.current
    val window = LocalActivity.current?.window
    val state by viewModel.state.collectAsState()
    val successState = state as? RecordSuccessState
    val dimBrightness = successState?.dimBrightness
    val isTouchOnlyWakes by rememberUpdatedState(successState?.isTouchOnlyWakes == true)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(viewModel) {
                awaitEachGesture {
                    val down = awaitFirstDown(
                        requireUnconsumed = false,
                        pass = PointerEventPass.Initial,
                    )
                    val isWakeOnly = isTouchOnlyWakes

                    viewModel.onScreenTouch()

                    if (isWakeOnly) {
                        down.consume()

                        do {
                            val event = awaitPointerEvent(PointerEventPass.Initial)
                            event.changes.forEach { change -> change.consume() }
                        } while (event.changes.any { change -> change.pressed })
                    }
                }
            },
    ) {
        when (val currentState = state) {
            is RecordSuccessState -> {
                if (currentState.gameActive) {
                    RecordSuccessScreen(
                        state = currentState,
                        bannerLoadCallback = viewModel.bannerLoadCallback,
                        onStartClick = viewModel::onStartClick,
                        onResumeClick = viewModel::onResumeClick,
                        onPauseClick = viewModel::onPauseClick,
                        onStopClick = viewModel::onStopClick,
                        onSoundClick = viewModel::onSoundClick,
                        onSettingsClick = viewModel::onSettingsClick,
                    )
                } else {
                    RecordInactiveGameScreen(currentState.storage, currentState.live.isLive)
                }
            }

            is UIState.Progress -> RecordProgressScreen()
            else -> unsupportedUI()
        }
    }

    SideEffect {
        view.keepScreenOn = true
    }

    DisposableEffect(viewModel) {
        view.keepScreenOn = true
        onDispose { view.keepScreenOn = false }
    }

    DisposableEffect(window, dimBrightness) {
        applyBrightness(window, dimBrightness ?: BRIGHTNESS_OVERRIDE_NONE)
        onDispose { applyBrightness(window, BRIGHTNESS_OVERRIDE_NONE) }
    }

    LifecycleResumeEffect(viewModel) {
        viewModel.onScreenTouch()
        onPauseOrDispose {}
    }
}

private fun applyBrightness(window: Window?, brightness: Float) {
    val attributes = window?.attributes ?: return

    attributes.screenBrightness = brightness
    window.attributes = attributes
}