package ua.frist008.action.record.data.infrastructure.mapper

import ua.frist008.action.record.util.common.ByteConst
import ua.frist008.action.record.util.common.FormatPattern
import java.util.Locale

object IpLongToStringMapper : (Long) -> String {

    override operator fun invoke(ip: Long): String =
        String.format(
            Locale.US,
            FormatPattern.IP4_ADDRESS,
            (ip shr ByteConst.BITS_3) and ByteConst.MAX_UBYTE_LONG,
            ((ip shr ByteConst.BITS_2)) and ByteConst.MAX_UBYTE_LONG,
            (ip shr ByteConst.BITS_1) and ByteConst.MAX_UBYTE_LONG,
            ip and ByteConst.MAX_UBYTE_LONG,
        )
}
