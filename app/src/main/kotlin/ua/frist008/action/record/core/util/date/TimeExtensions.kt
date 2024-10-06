package ua.frist008.action.record.core.util.date

import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.milliseconds

inline val timeNowMs get() = Clock.System.now().toEpochMilliseconds()
inline val durationNow get() = timeNowMs.milliseconds
