package ua.frist008.action.record.core.util.concurrent

import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeSource

suspend fun timer(
    durationMs: Long,
    delayEventMs: Long = durationMs,
    onNextEvent: suspend (msLeft: Duration) -> Unit = {},
    onFinished: suspend () -> Unit,
) {
    val startMark = TimeSource.Monotonic.markNow()
    val duration = durationMs.milliseconds
    val period = delayEventMs.milliseconds
    var nextTick = if (durationMs == delayEventMs) duration else Duration.ZERO

    while (true) {
        delay(nextTick - startMark.elapsedNow())

        val durationLeft = duration - startMark.elapsedNow()

        if (durationLeft <= Duration.ZERO) {
            onFinished()
            return
        }

        onNextEvent(durationLeft)
        nextTick += period
    }
}
