package ua.frist008.action.record.util.common

@Suppress("MemberVisibilityCanBePrivate") object NetworkDataConst {

    val KEY_0: Byte = 65
    val KEY_1: Byte = 72

    val FF00: Int = ShortConst.MAX_USHORT - ByteConst.MAX_UBYTE // 65 280 (65280)
    val FF00_LONG: Long = FF00.toLong()

    val FFFF: Long = ByteConst.MAX_BYTES_POW_3_LONG - ShortConst.MAX_USHORT // 16 711 679 (16711679)
    val FFFF_MINUS_1: Long = FFFF - 1 // 16 711 680 (16711680)

    const val BYTES_IN_KB = 1024
    const val BYTES_IN_MB = BYTES_IN_KB * BYTES_IN_KB // 1 048 576 (1048576)
}
