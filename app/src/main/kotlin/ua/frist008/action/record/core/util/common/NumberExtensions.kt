package ua.frist008.action.record.core.util.common

import kotlin.math.pow
import kotlin.math.roundToInt

/** The base a number is rounded in, to its decimal places */
private const val FloatDecimalBase = 10f

/** The base a number is rounded in, to its decimal places */
private const val DoubleDecimalBase = 10.0

fun Float?.round(decimalPoint: Int) = this?.round(decimalPoint)

fun Float.round(decimalPoint: Int): Float {
    val multiplier = FloatDecimalBase.pow(decimalPoint)
    return (this * multiplier).roundToInt() / multiplier
}

fun Double?.round(decimalPoint: Int) = this?.round(decimalPoint)

fun Double.round(decimalPoint: Int): Double {
    val multiplier = DoubleDecimalBase.pow(decimalPoint)
    return (this * multiplier).roundToInt() / multiplier
}
