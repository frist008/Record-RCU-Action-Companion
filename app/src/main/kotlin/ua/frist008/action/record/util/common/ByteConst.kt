package ua.frist008.action.record.util.common

import kotlin.math.pow

@Suppress("MemberVisibilityCanBePrivate") object ByteConst {

    const val BITS_1: Int = Byte.SIZE_BITS // 8
    const val BITS_2: Int = Byte.SIZE_BITS * 2 // 16
    const val BITS_3: Int = Byte.SIZE_BITS * 3 // 24

    val MAX_UBYTE: Int = UByte.MAX_VALUE.toInt() // 255
    val MAX_UBYTE_LONG: Long = MAX_UBYTE.toLong() // 255
    val MAX_BYTES_VALUE: Int = MAX_UBYTE + 1 // 256

    val MAX_BYTES_POW_3: Double = MAX_BYTES_VALUE.toDouble().pow(3.0) // 16 777 216 (16777216)
    val MAX_BYTES_POW_3_LONG: Long = MAX_BYTES_POW_3.toLong() // 16 777 216 (16777216)
}
