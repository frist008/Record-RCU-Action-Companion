package ua.frist008.action.record.core.util.date

import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

object DateUtils {

    /**
     * 99:99:99
     */
    private val SIMPLE_TIME_FORMATTER: DateTimeFormat<LocalTime> by lazy {
        LocalTime.Format { time(LocalTime.Formats.ISO) }
    }

    fun fromDuration(time: Duration): LocalTime =
        LocalTime.fromSecondOfDay(time.inWholeSeconds.toInt())

    fun toDuration(time: LocalTime): Duration =
        time.toSecondOfDay().seconds

    fun formatTime(time: LocalTime): String =
        time.format(SIMPLE_TIME_FORMATTER)
}
