package ua.frist008.action.record.data.network

/** Constants of the Mirillis Action! remote-control protocol the radar and the record session share */
object NetworkConst {

    /** First byte of every packet header, the letter `A` */
    const val KEY_0: Byte = 65

    /** Second byte of every packet header, the letter `H` */
    const val KEY_1: Byte = 72

    /** Packet type in byte 10 of a reply to the radar request */
    const val TYPE_RADAR_REPLY: Byte = 1

    /** Packet type in byte 10 of a status packet of the record session */
    const val TYPE_STATUS: Byte = 2

    const val ANY_HOST = "0.0.0.0"
    const val BROADCAST_HOST = "255.255.255.255"

    /** Port the phone receives the status packets of the record session on */
    const val STATUS_PORT = 4197

    /** Port the phone receives the replies to the radar request on */
    const val RADAR_REPLY_PORT = 4198

    /** Port the PC receives the radar request on */
    const val RADAR_REQUEST_PORT = 4199
}
