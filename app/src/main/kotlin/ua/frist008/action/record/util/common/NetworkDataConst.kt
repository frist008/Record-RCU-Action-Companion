package ua.frist008.action.record.util.common

@Suppress("MemberVisibilityCanBePrivate") object NetworkDataConst {

    val FF00: Int = ShortConst.MAX_USHORT - ByteConst.MAX_UBYTE // 65280
    val FF00_LONG: Long = FF00.toLong()

    val FFFF: Long = ByteConst.MAX_BYTES_POW_3 - ShortConst.MAX_USHORT // 16 711 679
    val FFFF_PLUS_1: Long = FFFF + 1 // 16 711 680
}
