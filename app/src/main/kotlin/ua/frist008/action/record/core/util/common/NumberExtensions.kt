package ua.frist008.action.record.core.util.common

import kotlin.math.pow
import kotlin.math.roundToInt

fun Float?.round(decimalPoint: Int) = this?.round(decimalPoint)

fun Float.round(decimalPoint: Int): Float {
    val floatDecimalBase = 10f
    val multiplier = floatDecimalBase.pow(decimalPoint)

    return (this * multiplier).roundToInt() / multiplier
}

fun Double?.round(decimalPoint: Int) = this?.round(decimalPoint)

fun Double.round(decimalPoint: Int): Double {
    val doubleDecimalBase = 10.0
    val multiplier = doubleDecimalBase.pow(decimalPoint)

    return (this * multiplier).roundToInt() / multiplier
}
