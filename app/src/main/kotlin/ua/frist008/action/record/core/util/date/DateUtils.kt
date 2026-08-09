package ua.frist008.action.record.core.util.date

import kotlin.time.Duration

object DateUtils {

    fun formatFullTime(time: Duration): String {
        val seconds = time.inWholeSeconds % 60
        val secondsStr = if (seconds > 9) "$seconds" else "0$seconds"
        val minutes = time.inWholeMinutes % 60
        val minutesStr = if (minutes > 9) "$minutes" else "0$minutes"
        val hours = time.inWholeHours
        val hoursStr = if (hours > 9) "$hours" else "0$hours"
        val secondsWithSuffix = if (hours > 99) "$secondsStr+" else secondsStr
        return "$hoursStr:$minutesStr:$secondsWithSuffix"
    }
}
