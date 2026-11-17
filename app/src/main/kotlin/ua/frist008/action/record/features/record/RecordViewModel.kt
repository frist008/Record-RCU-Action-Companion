package ua.frist008.action.record.features.record

import android.os.Vibrator
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.ads.mobile.sdk.banner.AdSize
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAd
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAdEventCallback
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import ua.frist008.action.record.R
import ua.frist008.action.record.analytics.Analytics
import ua.frist008.action.record.core.presentation.BaseViewModel
import ua.frist008.action.record.core.presentation.dependency.PresentationDependenciesDelegate
import ua.frist008.action.record.core.presentation.dependency.StateOwner.Companion.state
import ua.frist008.action.record.core.ui.UIState
import ua.frist008.action.record.core.util.sound.VibratorEffect
import ua.frist008.action.record.core.util.sound.vibrate
import ua.frist008.action.record.data.network.DisconnectException
import ua.frist008.action.record.data.network.NotConnectedException
import ua.frist008.action.record.features.NavCommand
import ua.frist008.action.record.features.record.entity.FixedRecordCommand
import ua.frist008.action.record.features.record.entity.RecordSuccessState
import ua.frist008.action.record.features.record.entity.StreamingRecordCommand
import java.net.BindException
import java.net.ConnectException
import java.net.SocketTimeoutException
import kotlin.time.Duration.Companion.seconds

@HiltViewModel(assistedFactory = RecordViewModel.Factory::class)
class RecordViewModel @AssistedInject constructor(
    @Assisted val pcId: Long,
    dependencies: PresentationDependenciesDelegate,
    private val recordRepository: RecordRepository,
    private val vibrator: Vibrator,
) : BaseViewModel(dependencies) {

    @AssistedFactory
    interface Factory {
        fun create(pcId: Long): RecordViewModel
    }

    private var autoBack: Job? = null
    private val stateMutex = Mutex()

    /**
     * The banner's load result: more than ten failed loads in a row switch its size. A click on a loaded banner switches
     * it too — the user leaves for the ad, and the record screen loads the other size once it is drawn again, where the
     * old SDK did it on `onAdClosed`; the Next-Gen SDK reports no close for a click that leaves the app. Its guide names
     * no thread for these callbacks, so the counter is atomic.
     */
    val bannerLoadCallback = object : AdLoadCallback<BannerAd> {

        private val errorCounter = atomic(0)

        override fun onAdLoaded(ad: BannerAd) {
            errorCounter.value = 0
            ad.adEventCallback = object : BannerAdEventCallback {
                override fun onAdClicked() = changeBannerSize()
            }
        }

        override fun onAdFailedToLoad(adError: LoadAdError) {
            if (errorCounter.incrementAndGet() > 10) {
                errorCounter.value = 0
                changeBannerSize()
            }
        }
    }

    init {
        launch {
            while (viewModelScope.isActive) {
                try {
                    recordRepository.connect(pcId)
                } catch (e: SocketTimeoutException) {
                    stateMutex.withLock {
                        mutableState.emit(UIState.Progress())
                        startAutoBack()
                    }
                    Timber.d(e)
                }
            }
        }

        launch {
            recordRepository.recordFlow.collectLatest {
                stateMutex.withLock {
                    if (it.connected) {
                        val state = it.toUI(state.value as? RecordSuccessState?)
                        mutableState.emit(state)
                        autoBack?.cancel()
                        autoBack = null
                        Analytics.log(state)
                    } else {
                        mutableState.emit(UIState.Progress())
                        startAutoBack()
                    }
                }
            }
        }

        launch(Dispatchers.IO) {
            Analytics.subscribeAndBlock()
        }
    }

    fun onStartClick(state: RecordSuccessState = state()) {
        vibrator.vibrate(VibratorEffect.PRIMITIVE_QUICK_RISE)

        launch {
            if (state.live.isLive) {
                recordRepository.sendCommand(FixedRecordCommand.START)
            } else {
                recordRepository.sendCommand(StreamingRecordCommand.START)
            }
        }
    }

    fun onPauseClick() {
        vibrator.vibrate(VibratorEffect.PRIMITIVE_SLOW_RISE)

        launch {
            recordRepository.sendCommand(FixedRecordCommand.PAUSE)
        }
    }

    fun onResumeClick() {
        vibrator.vibrate(VibratorEffect.PRIMITIVE_QUICK_RISE)

        launch {
            recordRepository.sendCommand(FixedRecordCommand.RESUME)
        }
    }

    fun onStopClick(state: RecordSuccessState = state()) {
        vibrator.vibrate(VibratorEffect.PRIMITIVE_CLICK)

        launch {
            if (state.live.isLive) {
                recordRepository.sendCommand(FixedRecordCommand.STOP)
            } else {
                recordRepository.sendCommand(StreamingRecordCommand.STOP)
            }
        }
    }

    private fun startAutoBack() {
        autoBack = launch {
            delay(10.seconds)
            navigator.emit(NavCommand.BackCommand())
        }
    }

    private fun changeBannerSize() {
        launch {
            stateMutex.withLock {
                val state = state.value as? RecordSuccessState? ?: return@launch
                val oldSize = state.adSize
                val newSize = if (oldSize == AdSize.BANNER) AdSize.LARGE_BANNER else AdSize.BANNER
                mutableState.emit(state.copy(adSize = newSize))
            }
        }
    }

    override suspend fun onFailure(cause: Throwable) {
        when (cause) {
            is DisconnectException -> navigator.emit(NavCommand.BackCommand())

            is NotConnectedException,
            is SocketTimeoutException,
                -> toastFlow.emit(R.string.record_error_not_connected)

            is BindException -> {
                toastFlow.emit(R.string.record_error_port_busy)
                navigator.emit(NavCommand.BackCommand())
            }

            is ConnectException -> {
                toastFlow.emit(R.string.record_error_already_used)
                navigator.emit(NavCommand.BackCommand())
            }

            else -> {
                toastFlow.emit(R.string.error_unknown_error)
                navigator.emit(NavCommand.BackCommand())
                super.onFailure(cause)
            }
        }
    }
}
