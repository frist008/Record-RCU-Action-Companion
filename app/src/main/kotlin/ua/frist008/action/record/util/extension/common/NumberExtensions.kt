package ua.frist008.action.record.util.extension.common

import kotlin.math.pow
import kotlin.math.roundToInt

fun Float?.round(decimalPoint: Int) = this?.round(decimalPoint)

fun Float.round(decimalPoint: Int): Float {
    val multiplier = 10f.pow(decimalPoint)
    return (this * multiplier).roundToInt() / multiplier
}

fun Double?.round(decimalPoint: Int) = this?.round(decimalPoint)

fun Double.round(decimalPoint: Int): Double {
    val multiplier = 10.0.pow(decimalPoint)
    return (this * multiplier).roundToInt() / multiplier
}
