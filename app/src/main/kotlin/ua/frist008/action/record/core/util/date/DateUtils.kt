package ua.frist008.action.record.core.util.date

import kotlin.time.Duration

object DateUtils {

    private const val SECONDS_IN_MINUTE = 60
    private const val MINUTES_IN_HOUR = 60

    /** The largest value written with one digit, so a smaller one gets a leading zero */
    private const val MAX_ONE_DIGIT = 9

    /** The largest number of hours the time shows whole, so a larger one gets a `+` */
    private const val MAX_HOURS = 99

    fun formatFullTime(time: Duration): String {
        val seconds = time.inWholeSeconds % SECONDS_IN_MINUTE
        val secondsStr = if (seconds > MAX_ONE_DIGIT) "$seconds" else "0$seconds"
        val minutes = time.inWholeMinutes % MINUTES_IN_HOUR
        val minutesStr = if (minutes > MAX_ONE_DIGIT) "$minutes" else "0$minutes"
        val hours = time.inWholeHours
        val hoursStr = if (hours > MAX_ONE_DIGIT) "$hours" else "0$hours"
        val secondsWithSuffix = if (hours > MAX_HOURS) "$secondsStr+" else secondsStr

        return "$hoursStr:$minutesStr:$secondsWithSuffix"
    }
}
