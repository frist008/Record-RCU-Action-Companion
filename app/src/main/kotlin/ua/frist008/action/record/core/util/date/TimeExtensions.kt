package ua.frist008.action.record.core.util.date

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.milliseconds

inline val timeNow get() = Clock.System.now()
inline val timeNowMs get() = timeNow.toEpochMilliseconds()
inline val durationNow get() = timeNowMs.milliseconds
inline val localDateTimeNow get() = timeNow.toLocalDateTime(TimeZone.UTC)
inline val localTimeNow get() = localDateTimeNow.time

inline val localTimeSecondsNow
    get() = localTimeNow.let { time ->
        LocalTime(time.hour, time.minute, time.second)
    }

