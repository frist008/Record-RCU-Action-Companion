package ua.frist008.action.record.util.extension.network

import ua.frist008.action.record.util.common.FormatPattern
import java.util.Locale

val Long.toIPStr: String
    get() = String.format(
        Locale.US,
        FormatPattern.IP4_ADDRESS,
        (this shr Byte.SIZE_BITS * 3) and UByte.MAX_VALUE.toLong(),
        (this shr Byte.SIZE_BITS * 2) and UByte.MAX_VALUE.toLong(),
        (this shr Byte.SIZE_BITS) and UByte.MAX_VALUE.toLong(),
        this and UByte.MAX_VALUE.toLong(),
    )
