package ua.frist008.action.record.core.util.common

import androidx.annotation.CheckResult

@CheckResult public fun <T> T?.takeIfNotNull(): T? = this?.takeIf { true }
