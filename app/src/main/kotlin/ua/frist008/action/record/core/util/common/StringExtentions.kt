package ua.frist008.action.record.core.util.common

import androidx.annotation.CheckResult

@CheckResult public fun String?.takeIfNotEmpty(): String? = this?.takeIf { it.isNotEmpty() }

@CheckResult public fun String?.takeIfEqual(target: String?): String? =
    this?.takeIf { it == target }

@CheckResult public fun String?.takeIfEqual(target: () -> String?): String? =
    this?.takeIf { it == target() }

@CheckResult public fun String?.takeIfNot(target: String?): String? = this?.takeIf { it != target }

@CheckResult public fun String?.takeIfNot(target: () -> String?): String? =
    this?.takeIf { it != target() }
