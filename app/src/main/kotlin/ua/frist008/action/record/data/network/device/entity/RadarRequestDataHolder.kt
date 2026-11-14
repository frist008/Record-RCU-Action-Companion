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

    private val clientDataArr = ByteArray(REQUEST_SIZE)

    init {
        val sequence = Random.nextInt(Short.MAX_VALUE.toInt())
        val replyPort = NetworkConst.RADAR_REPLY_PORT

        clientDataArr[0] = NetworkConst.KEY_0
        clientDataArr[1] = NetworkConst.KEY_1
        clientDataArr[2] = (sequence shr ByteConst.BITS_1).toByte()
        clientDataArr[3] = sequence.toByte()
        clientDataArr[8] = (replyPort shr ByteConst.BITS_1).toByte()
        clientDataArr[9] = replyPort.toByte()
        phoneIdArr?.copyInto(clientDataArr, destinationOffset = 10, endIndex = Int.SIZE_BYTES)
    }

    /**
     * Builds the request sent from one address of the phone.
     *
     * @param localAddressArr the four bytes of the IPv4 address of the phone, in network order.
     * @return the bytes of the request.
     */
    fun newClientDataInstance(localAddressArr: ByteArray): ByteArray {
        val dataArr = clientDataArr.copyOf()

        localAddressArr.copyInto(dataArr, destinationOffset = 4) // dataArr[4-7]

        return dataArr
    }

    companion object {

        suspend fun newInstance() = RadarRequestDataHolder(FirebaseId.getIdMurmurHash3())

        private const val REQUEST_SIZE = 32
    }
}