package ua.frist008.action.record.util.extension.concurrent

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.TickerMode
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeSource

suspend fun timer(
    durationMs: Long,
    delayEventMs: Long = durationMs,
    onNextEvent: suspend (msLeft: Duration) -> Unit = {},
    onFinished: suspend () -> Unit,
) {
    val startTime = TimeSource.Monotonic.markNow()
    val initialDelayMillis = if (durationMs == delayEventMs) durationMs else 0
    val tickerChannel = ticker(
        delayMillis = delayEventMs,
        initialDelayMillis = initialDelayMillis,
        context = Dispatchers.IO,
        mode = TickerMode.FIXED_PERIOD,
    )

    tickerChannel.receiveAsFlow().collectLatest {
        val msPassed = TimeSource.Monotonic.markNow() - startTime
        val durationLeft = durationMs.milliseconds - msPassed

        if (durationLeft <= 0.milliseconds) {
            tickerChannel.cancel()
            onFinished()
        } else {
            onNextEvent(durationLeft)
        }
    }
}
