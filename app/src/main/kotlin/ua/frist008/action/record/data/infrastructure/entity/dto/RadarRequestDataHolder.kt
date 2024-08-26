package ua.frist008.action.record.data.infrastructure.entity.dto

import ua.frist008.action.record.util.common.ByteConst
import ua.frist008.action.record.util.common.FirebaseId
import ua.frist008.action.record.util.common.NetworkDataConst

class RadarRequestDataHolder private constructor(androidIdArr: ByteArray?) {

    private val clientDataArr = ByteArray(32)

    init {
        val randomInt = (Math.random() * Short.MAX_VALUE).toInt()
        clientDataArr[0] = NetworkDataConst.KEY_0
        clientDataArr[1] = NetworkDataConst.KEY_1
        clientDataArr[2] = ((randomInt shr ByteConst.BITS_1) and ByteConst.MAX_UBYTE).toByte()
        clientDataArr[3] = (randomInt and ByteConst.MAX_UBYTE).toByte()

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
            (((clientDataArr[2].toInt() shl Byte.SIZE_BITS) and NetworkDataConst.FF00) or
                (clientDataArr[3].toInt() and ByteConst.MAX_UBYTE)) > Short.MAX_VALUE

    companion object {

        suspend fun newInstance() = RadarRequestDataHolder(FirebaseId.getIdMurmurHash3())
    }
}
