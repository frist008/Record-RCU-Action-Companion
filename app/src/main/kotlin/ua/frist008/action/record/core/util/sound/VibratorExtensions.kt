package ua.frist008.action.record.core.util.sound

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import timber.log.Timber

fun Vibrator.vibrate(vibratorEffect: VibratorEffect) {
    try {
        if (!hasVibrator()) return

        val effect = vibratorEffect.effectClick

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
                if (areAllPrimitivesSupported(effect)) {
                    vibrate(
                        VibrationEffect.startComposition()
                            .addPrimitive(effect)
                            .compose(),
                    )
                } else {
                    vibrate(VibrationEffect.createOneShot(VibratorEffect.DEFAULT_DURATION, effect))
                }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ->
                vibrate(VibrationEffect.createPredefined(effect))

            else -> vibrate(VibrationEffect.createOneShot(VibratorEffect.DEFAULT_DURATION, effect))
        }
    } catch (e: Throwable) {
        Timber.e(e)
    }
}

@Suppress("unused")
enum class VibratorEffect(val effectClick: Int) {
    PRIMITIVE_CLICK(
        effectClick = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            VibrationEffect.Composition.PRIMITIVE_CLICK
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            VibrationEffect.EFFECT_HEAVY_CLICK
        } else {
            VibrationEffect.DEFAULT_AMPLITUDE
        },
    ),
    PRIMITIVE_THUD(
        effectClick = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            VibrationEffect.Composition.PRIMITIVE_THUD
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            VibrationEffect.EFFECT_HEAVY_CLICK
        } else {
            VibrationEffect.DEFAULT_AMPLITUDE
        },
    ),
    PRIMITIVE_SPIN(
        effectClick = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            VibrationEffect.Composition.PRIMITIVE_SPIN
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            VibrationEffect.EFFECT_HEAVY_CLICK
        } else {
            VibrationEffect.DEFAULT_AMPLITUDE
        },
    ),
    PRIMITIVE_QUICK_RISE(
        effectClick = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            VibrationEffect.Composition.PRIMITIVE_QUICK_RISE
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            VibrationEffect.EFFECT_HEAVY_CLICK
        } else {
            VibrationEffect.DEFAULT_AMPLITUDE
        },
    ),
    PRIMITIVE_SLOW_RISE(
        effectClick = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            VibrationEffect.Composition.PRIMITIVE_SLOW_RISE
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            VibrationEffect.EFFECT_HEAVY_CLICK
        } else {
            VibrationEffect.DEFAULT_AMPLITUDE
        },
    ),
    PRIMITIVE_QUICK_FALL(
        effectClick = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            VibrationEffect.Composition.PRIMITIVE_QUICK_FALL
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            VibrationEffect.EFFECT_HEAVY_CLICK
        } else {
            VibrationEffect.DEFAULT_AMPLITUDE
        },
    ),
    PRIMITIVE_TICK(
        effectClick = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            VibrationEffect.Composition.PRIMITIVE_TICK
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            VibrationEffect.EFFECT_HEAVY_CLICK
        } else {
            VibrationEffect.DEFAULT_AMPLITUDE
        },
    ),
    PRIMITIVE_LOW_TICK(
        effectClick = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            VibrationEffect.Composition.PRIMITIVE_LOW_TICK
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            VibrationEffect.EFFECT_HEAVY_CLICK
        } else {
            VibrationEffect.DEFAULT_AMPLITUDE
        },
    ),
    ;

    companion object {
        const val DEFAULT_DURATION = 100L
    }
}
