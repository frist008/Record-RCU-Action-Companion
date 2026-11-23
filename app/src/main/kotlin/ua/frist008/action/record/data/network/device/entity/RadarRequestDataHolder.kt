package ua.frist008.action.record.data.network.device.entity

import ua.frist008.action.record.core.util.common.ByteConst
import ua.frist008.action.record.data.network.FirebaseId
import ua.frist008.action.record.data.network.NetworkConst
import kotlin.random.Random

/**
 * Holds the radar request of the phone: the header with a sequence number below 32768, the port
 * the PC replies to and the id of the phone, fixed for one scan.
 */
class RadarRequestDataHolder private constructor(phoneIdArr: ByteArray?) {

    private val clientDataArr: ByteArray

    init {
        val requestSize = 32
        val phoneIdIndex = 10
        val sequence = Random.nextInt(Short.MAX_VALUE.toInt())
        val replyPort = NetworkConst.RADAR_REPLY_PORT

        clientDataArr = ByteArray(requestSize)
        clientDataArr[0] = NetworkConst.KEY_0 // clientDataArr[0-1] key
        clientDataArr[1] = NetworkConst.KEY_1
        // clientDataArr[2-3] sequence
        clientDataArr[NetworkConst.SEQUENCE_INDEX] = (sequence shr ByteConst.BITS_1).toByte()
        clientDataArr[NetworkConst.SEQUENCE_INDEX + 1] = sequence.toByte()
        // clientDataArr[4-7] is the address of the phone, written by newClientDataInstance
        // clientDataArr[8-9] is the port the PC replies to, 4198: 16, 102
        clientDataArr[NetworkConst.PORT_INDEX] = (replyPort shr ByteConst.BITS_1).toByte()
        clientDataArr[NetworkConst.PORT_INDEX + 1] = replyPort.toByte()
        // clientDataArr[10-13] is the id of the phone: its first four bytes
        phoneIdArr?.copyInto(
            destination = clientDataArr,
            destinationOffset = phoneIdIndex,
            endIndex = Int.SIZE_BYTES,
        )
    }

    /**
     * Builds the request sent from one address of the phone.
     *
     * @param localAddressArr the four bytes of the IPv4 address of the phone, in network order.
     * @return the bytes of the request.
     */
    fun newClientDataInstance(localAddressArr: ByteArray): ByteArray {
        val dataArr = clientDataArr.copyOf()

        localAddressArr.copyInto(dataArr, destinationOffset = NetworkConst.ADDRESS_INDEX)

        return dataArr
    }

    companion object {

        suspend fun newInstance() = RadarRequestDataHolder(FirebaseId.getIdMurmurHash3())
    }
}
