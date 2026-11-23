package ua.frist008.action.record.core.util.date

import kotlin.time.Duration

object DateUtils {

    fun formatFullTime(time: Duration): String {
        val secondsInMinute = 60
        val minutesInHour = 60
        val maxOneDigit = 9 // a smaller value gets a leading zero
        val maxHours = 99 // a larger number of hours gets a `+`

        val seconds = time.inWholeSeconds % secondsInMinute
        val secondsStr = if (seconds > maxOneDigit) "$seconds" else "0$seconds"
        val minutes = time.inWholeMinutes % minutesInHour
        val minutesStr = if (minutes > maxOneDigit) "$minutes" else "0$minutes"
        val hours = time.inWholeHours
        val hoursStr = if (hours > maxOneDigit) "$hours" else "0$hours"
        val secondsWithSuffix = if (hours > maxHours) "$secondsStr+" else secondsStr

        return "$hoursStr:$minutesStr:$secondsWithSuffix"
    }
}
