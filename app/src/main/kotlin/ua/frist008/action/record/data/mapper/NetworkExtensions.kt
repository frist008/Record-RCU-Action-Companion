package ua.frist008.action.record.data.mapper

import ua.frist008.action.record.core.util.common.ByteConst
import ua.frist008.action.record.core.util.common.FormatPattern
import java.util.Locale

val Long.toIPStr: String
    get() = String.format(
        Locale.US,
        FormatPattern.IP4_ADDRESS,
        (this shr ByteConst.BITS_3) and ByteConst.MAX_UBYTE_LONG,
        (this shr ByteConst.BITS_2) and ByteConst.MAX_UBYTE_LONG,
        (this shr ByteConst.BITS_1) and ByteConst.MAX_UBYTE_LONG,
        this and ByteConst.MAX_UBYTE_LONG,
    )
