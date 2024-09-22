package ua.frist008.action.record.core.util.common

import androidx.annotation.CheckResult

@CheckResult public fun <T> List<T>?.takeIfNotEmpty(): List<T>? = this?.takeIf { it.isNotEmpty() }
