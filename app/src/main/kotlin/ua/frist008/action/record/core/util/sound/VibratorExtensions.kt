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

            else -> vibrate(VibrationEffect.createPredefined(effect))
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
        } else {
            VibrationEffect.EFFECT_HEAVY_CLICK
        },
    ),
    PRIMITIVE_THUD(
        effectClick = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            VibrationEffect.Composition.PRIMITIVE_THUD
        } else {
            VibrationEffect.EFFECT_HEAVY_CLICK
        },
    ),
    PRIMITIVE_SPIN(
        effectClick = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            VibrationEffect.Composition.PRIMITIVE_SPIN
        } else {
            VibrationEffect.EFFECT_HEAVY_CLICK
        },
    ),
    PRIMITIVE_QUICK_RISE(
        effectClick = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            VibrationEffect.Composition.PRIMITIVE_QUICK_RISE
        } else {
            VibrationEffect.EFFECT_HEAVY_CLICK
        },
    ),
    PRIMITIVE_SLOW_RISE(
        effectClick = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            VibrationEffect.Composition.PRIMITIVE_SLOW_RISE
        } else {
            VibrationEffect.EFFECT_HEAVY_CLICK
        },
    ),
    PRIMITIVE_QUICK_FALL(
        effectClick = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            VibrationEffect.Composition.PRIMITIVE_QUICK_FALL
        } else {
            VibrationEffect.EFFECT_HEAVY_CLICK
        },
    ),
    PRIMITIVE_TICK(
        effectClick = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            VibrationEffect.Composition.PRIMITIVE_TICK
        } else {
            VibrationEffect.EFFECT_HEAVY_CLICK
        },
    ),
    PRIMITIVE_LOW_TICK(
        effectClick = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            VibrationEffect.Composition.PRIMITIVE_LOW_TICK
        } else {
            VibrationEffect.EFFECT_HEAVY_CLICK
        },
    ),
    ;

    companion object {
        const val DEFAULT_DURATION = 100L
    }
}
