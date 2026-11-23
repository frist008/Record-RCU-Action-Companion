package ua.frist008.action.record.data.network

/** Constants of the Action! remote-control protocol the radar and the record session share */
object NetworkConst {

    /** First byte of every packet header, the letter `A` */
    const val KEY_0: Byte = 65

    /** Second byte of every packet header, the letter `H` */
    const val KEY_1: Byte = 72

    /** Index of the two bytes of the sequence number in every packet header */
    const val SEQUENCE_INDEX = 2

    /** Index of the four bytes of the IPv4 address in a radar request and in its reply */
    const val ADDRESS_INDEX = 4

    /** Index of the two bytes of the port in a radar request and in its reply */
    const val PORT_INDEX = 8

    /** Index of the packet type byte in a reply to the radar request and in a status packet */
    const val TYPE_INDEX = 10

    const val ANY_HOST = "0.0.0.0"
    const val BROADCAST_HOST = "255.255.255.255"

    /** Port the phone receives the replies to the radar request on */
    const val RADAR_REPLY_PORT = 4198
}
