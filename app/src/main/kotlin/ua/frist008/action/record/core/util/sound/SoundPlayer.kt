package ua.frist008.action.record.core.util.sound

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.annotation.RawRes
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ua.frist008.action.record.R
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Plays the signals of the app from its own sound files, so they sound the same on every phone; a
 * mute silences them until the app is closed.
 */
@Singleton class SoundPlayer @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {

    private val isMutedMutableFlow = MutableStateFlow(false)

    /** Whether the sounds are muted from the app bar of the record screen */
    val isMutedFlow: StateFlow<Boolean> = isMutedMutableFlow.asStateFlow()

    private val soundPool by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        SoundPool.Builder()
            .setMaxStreams(AppSound.entries.size)
            .setAudioAttributes(attributes)
            .build()
    }

    /** The id of each signal in [soundPool]: the first read starts loading the files */
    private val soundIds by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        AppSound.entries.associateWith { sound -> soundPool.load(context, sound.rawRes, 1) }
    }

    fun toggleMute() {
        isMutedMutableFlow.value = !isMutedMutableFlow.value
    }

    /**
     * Starts loading the sound files, so the first signal plays at once; it reads the files, so it
     * is called off the main thread. A signal played before its file is loaded stays silent.
     */
    fun load() {
        soundIds
    }

    /**
     * Plays the sound unless the sounds are muted.
     *
     * @param sound the signal to play.
     */
    fun play(sound: AppSound) {
        if (isMutedMutableFlow.value) return

        soundPool.play(soundIds.getValue(sound), 1f, 1f, 1, 0, 1f)
    }
}

/**
 * A signal of the app.
 *
 * @property rawRes the sound file it plays.
 */
enum class AppSound(@param:RawRes val rawRes: Int) {
    RECORD(R.raw.sound_record),
    PAUSE(R.raw.sound_pause),
    STOP(R.raw.sound_stop),
    LOW_SPACE(R.raw.sound_low_space),
    DURATION(R.raw.sound_duration),
}
