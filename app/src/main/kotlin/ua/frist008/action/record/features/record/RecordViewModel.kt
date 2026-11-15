package ua.frist008.action.record.features.record

import android.os.Vibrator
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.ads.mobile.sdk.banner.AdSize
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAd
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAdEventCallback
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError
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
import ua.frist008.action.record.core.util.sound.AppSound
import ua.frist008.action.record.core.util.sound.SoundPlayer
import ua.frist008.action.record.core.util.sound.VibratorEffect
import ua.frist008.action.record.core.util.sound.vibrate
import ua.frist008.action.record.data.network.DisconnectException
import ua.frist008.action.record.data.network.NotConnectedException
import ua.frist008.action.record.features.NavCommand
import ua.frist008.action.record.features.NewRoot
import ua.frist008.action.record.features.record.entity.ErrorType
import ua.frist008.action.record.features.record.entity.FixedRecordCommand
import ua.frist008.action.record.features.record.entity.RecordDomainEntity
import ua.frist008.action.record.features.record.entity.RecordSuccessState
import ua.frist008.action.record.features.record.entity.StreamingRecordCommand
import ua.frist008.action.record.features.settings.SettingsRepository
import ua.frist008.action.record.features.settings.entity.SettingsDomainEntity
import java.net.BindException
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@HiltViewModel class RecordViewModel @Inject constructor(
    dependencies: PresentationDependenciesDelegate,
    private val recordRepository: RecordRepository,
    private val settingsRepository: SettingsRepository,
    private val soundPlayer: SoundPlayer,
    private val vibrator: Vibrator,
) : BaseViewModel(dependencies) {

    private val autoBackJob = atomic<Job?>(null)
    private val dimJob = atomic<Job?>(null)
    private val stateMutex = Mutex()
    private val settings = atomic(SettingsDomainEntity())

    /** The recording time of the last packet, null before the first one */
    private val lastDuration = atomic<Duration?>(null)

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
                    recordRepository.connect()
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
                        showRecord(it)
                    } else {
                        mutableState.emit(UIState.Progress())
                        startAutoBack()
                    }
                }
            }
        }

        launch {
            settingsRepository.settingsFlow.collect { newSettings ->
                settings.value = newSettings
                updateSuccessState { state ->
                    state.copy(isFirstTouchOnlyWakes = newSettings.isFirstTouchOnlyWakes)
                }
            }
        }

        launch {
            soundPlayer.isMutedFlow.collect { isMuted ->
                updateSuccessState { state -> state.copy(isSoundMuted = isMuted) }
            }
        }

        launch(Dispatchers.IO) {
            Analytics.subscribeAndBlock()
        }
    }

    fun onStartClick(state: RecordSuccessState = state()) {
        vibrator.vibrate(VibratorEffect.PRIMITIVE_QUICK_RISE)
        playButtonSound(settings.value.isRecordButtonSound, AppSound.RECORD)

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
        playButtonSound(settings.value.isPauseButtonSound, AppSound.PAUSE)

        launch {
            recordRepository.sendCommand(FixedRecordCommand.PAUSE)
        }
    }

    fun onResumeClick() {
        vibrator.vibrate(VibratorEffect.PRIMITIVE_QUICK_RISE)
        playButtonSound(settings.value.isRecordButtonSound, AppSound.RECORD)

        launch {
            recordRepository.sendCommand(FixedRecordCommand.RESUME)
        }
    }

    fun onStopClick(state: RecordSuccessState = state()) {
        vibrator.vibrate(VibratorEffect.PRIMITIVE_CLICK)
        playButtonSound(settings.value.isStopButtonSound, AppSound.STOP)

        launch {
            if (state.live.isLive) {
                recordRepository.sendCommand(FixedRecordCommand.STOP)
            } else {
                recordRepository.sendCommand(StreamingRecordCommand.STOP)
            }
        }
    }

    fun onSettingsClick() {
        launch {
            navigator.emit(NavCommand.SettingsScreen)
        }
    }

    fun onSoundClick() {
        soundPlayer.toggleMute()
    }

    /** Restores the brightness of the dimmed screen and counts the time without touches again */
    fun onScreenTouch() {
        launch(dimJob) {
            val currentSettings = settings.value

            updateSuccessState { state -> state.copy(dimBrightness = null) }
            delay(currentSettings.dimAfter)
            updateSuccessState { state ->
                state.copy(dimBrightness = currentSettings.dimToPercent / PERCENT_MAX)
            }
        }
    }

    private suspend fun showRecord(domain: RecordDomainEntity) {
        val previousState = state.value as? RecordSuccessState
        val currentSettings = settings.value
        val newState = domain.toUI(previousState, currentSettings.minFreeSpace).let { state ->
            if (previousState == null) {
                state.copy(
                    isSoundMuted = soundPlayer.isMutedFlow.value,
                    isFirstTouchOnlyWakes = currentSettings.isFirstTouchOnlyWakes,
                )
            } else {
                state
            }
        }

        mutableState.emit(newState)
        autoBackJob.value?.cancel()
        Analytics.log(newState)
        playSignals(previousState, newState, domain.duration, currentSettings)

        if (previousState == null) onScreenTouch()
    }

    private fun playSignals(
        previousState: RecordSuccessState?,
        newState: RecordSuccessState,
        duration: Duration,
        currentSettings: SettingsDomainEntity,
    ) {
        val isLowSpace = newState.storage.errorType == ErrorType.ERROR
        val wasLowSpace = previousState?.storage?.errorType == ErrorType.ERROR

        if (currentSettings.isLowSpaceSound && isLowSpace && !wasLowSpace) {
            soundPlayer.play(AppSound.LOW_SPACE)
        }

        val previousDuration = lastDuration.getAndSet(duration) ?: return
        val soundAfter = currentSettings.durationSoundAfter

        val isSoundAfterReached = previousDuration < soundAfter && duration >= soundAfter

        if (currentSettings.isDurationSound && isSoundAfterReached) {
            soundPlayer.play(AppSound.DURATION)
        }
    }

    private fun playButtonSound(isEnabled: Boolean, sound: AppSound) {
        if (isEnabled) soundPlayer.play(sound)
    }

    private suspend fun updateSuccessState(transform: (RecordSuccessState) -> RecordSuccessState) {
        stateMutex.withLock {
            val state = state.value as? RecordSuccessState ?: return
            mutableState.emit(transform(state))
        }
    }

    /** Opens the search when the PC stays out of reach for [AUTO_BACK_DELAY], keeping a countdown */
    private fun startAutoBack() {
        if (autoBackJob.value?.isActive != true) {
            launch(autoBackJob) {
                delay(AUTO_BACK_DELAY)
                openConnection()
            }
        }
    }

    private suspend fun openConnection() {
        navigator.emit(NewRoot(NavCommand.ConnectionScreen))
    }

    private fun changeBannerSize() {
        launch {
            updateSuccessState { state ->
                val isBanner = state.adSize == AdSize.BANNER
                state.copy(adSize = if (isBanner) AdSize.LARGE_BANNER else AdSize.BANNER)
            }
        }
    }

    override suspend fun onFailure(cause: Throwable) {
        when (cause) {
            is DisconnectException -> openConnection()

            is NotConnectedException,
            is SocketTimeoutException,
                -> toastFlow.emit(R.string.record_error_not_connected)

            is BindException -> {
                toastFlow.emit(R.string.record_error_port_busy)
                delay(RECONNECT_DELAY)
                openConnection()
            }

            is ConnectException -> {
                toastFlow.emit(R.string.record_error_already_used)
                delay(RECONNECT_DELAY)
                openConnection()
            }

            else -> {
                toastFlow.emit(R.string.error_unknown_error)
                super.onFailure(cause)
                delay(RECONNECT_DELAY)
                openConnection()
            }
        }
    }

    companion object {

        private val AUTO_BACK_DELAY = 10.seconds

        /** Pause before the search, which reconnects at once, against a loop of refusals */
        private val RECONNECT_DELAY = 5.seconds

        /** The percent of the full brightness, as the window takes it from 0 to 1 */
        private const val PERCENT_MAX = 100f
    }
}
