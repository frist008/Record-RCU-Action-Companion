package ua.frist008.action.record.core.util.sound

import android.media.AudioManager
import android.media.MediaActionSound
import android.media.ToneGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Plays the signals of the app with the sounds of the system, so it ships no audio file; a mute
 * silences them until the app is closed.
 */
@Singleton class SoundPlayer @Inject constructor() {

    private val isMutedMutableFlow = MutableStateFlow(false)

    /** Whether the sounds are muted from the app bar of the record screen */
    val isMutedFlow: StateFlow<Boolean> = isMutedMutableFlow.asStateFlow()

    private val mediaActionSound by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        MediaActionSound().apply {
            AppSound.entries.forEach { sound -> sound.actionSound?.let(::load) }
        }
    }

    private val toneGenerator by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        runCatching { ToneGenerator(AudioManager.STREAM_NOTIFICATION, TONE_VOLUME) }
            .onFailure(Timber::w)
            .getOrNull()
    }

    fun toggleMute() {
        isMutedMutableFlow.value = !isMutedMutableFlow.value
    }

    /**
     * Plays the sound unless the sounds are muted.
     *
     * @param sound the signal to play.
     */
    fun play(sound: AppSound) {
        if (isMutedMutableFlow.value) return

        val actionSound = sound.actionSound

        if (actionSound != null) {
            mediaActionSound.play(actionSound)
        } else {
            toneGenerator?.startTone(sound.tone, TONE_DURATION_MS)
        }
    }

    companion object {

        private const val TONE_VOLUME = 80
        private const val TONE_DURATION_MS = 600
    }
}

/**
 * A signal of the app.
 *
 * @property actionSound the sound of the camera of the system it plays, or null for a [tone].
 * @property tone the tone of [ToneGenerator] it plays when it has no [actionSound].
 */
enum class AppSound(val actionSound: Int?, val tone: Int) {
    RECORD(
        actionSound = MediaActionSound.START_VIDEO_RECORDING,
        tone = ToneGenerator.TONE_PROP_ACK,
    ),
    PAUSE(actionSound = MediaActionSound.FOCUS_COMPLETE, tone = ToneGenerator.TONE_PROP_ACK),
    STOP(actionSound = MediaActionSound.STOP_VIDEO_RECORDING, tone = ToneGenerator.TONE_PROP_ACK),
    LOW_SPACE(actionSound = null, tone = ToneGenerator.TONE_SUP_ERROR),
    DURATION(actionSound = null, tone = ToneGenerator.TONE_PROP_BEEP2),
}
