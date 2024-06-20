package ua.frist008.action.record.data.infrastructure.entity.dto

import ua.frist008.action.record.util.common.FirebaseId

class RadarRequestDataHolder private constructor(androidIdArr: ByteArray?) {

    private val clientDataArr = ByteArray(32)

    init {
        val randomInt = (Math.random() * Short.MAX_VALUE).toInt()
        clientDataArr[0] = 65
        clientDataArr[1] = 72
        clientDataArr[2] = ((randomInt shr Byte.SIZE_BITS) and MAX_UBYTE).toByte()
        clientDataArr[3] = (randomInt and MAX_UBYTE).toByte()

        clientDataArr[8] = 16
        clientDataArr[9] = 102

        if (androidIdArr != null) {
            clientDataArr[10] = androidIdArr[0]
            clientDataArr[11] = androidIdArr[1]
            clientDataArr[12] = androidIdArr[2]
            clientDataArr[13] = androidIdArr[3]
        }
    }

    fun newClientDataInstance(): ByteArray = clientDataArr.copyOf()

    fun isResponseValid(clientDataArr: ByteArray): Boolean = // clientDataArr[0-3]
        clientDataArr[0] == this.clientDataArr[0] &&
            clientDataArr[1] == this.clientDataArr[1] &&
            (((clientDataArr[2].toInt() shl Byte.SIZE_BITS) and FF00) or
                (clientDataArr[3].toInt() and MAX_UBYTE)) > Short.MAX_VALUE

    companion object {

        private val MAX_UBYTE = UByte.MAX_VALUE.toInt() // 255
        private val MAX_USHORT = UShort.MAX_VALUE.toInt() // 65535
        private val FF00 = MAX_USHORT - MAX_UBYTE // 65280

        suspend fun newInstance() = RadarRequestDataHolder(FirebaseId.getIdMurmurHash3())
    }
}
