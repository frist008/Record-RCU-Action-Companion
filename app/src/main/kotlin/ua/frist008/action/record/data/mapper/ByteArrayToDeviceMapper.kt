package ua.frist008.action.record.data.mapper

import ua.frist008.action.record.data.network.NetworkConst
import ua.frist008.action.record.data.network.device.entity.DeviceDTO

/** Reads the PC from its reply to the radar request; `null` when the packet is no such reply */
object ByteArrayToDeviceMapper : (ByteArray, Int) -> DeviceDTO? {

    override operator fun invoke(dataArr: ByteArray, size: Int): DeviceDTO? {
        val replyMinSize = 10
        val typeRadarReply: Byte = 1 // the packet type in byte 10 of a reply to the radar request

        if (size < replyMinSize || !ByteArrayToIsFromPcMapper(dataArr, size)) return null

        val isNamed = size > replyMinSize && dataArr[NetworkConst.TYPE_INDEX] == typeRadarReply

        return DeviceDTO(
            name = if (isNamed) ByteArrayToNameMapper(dataArr, size) else null, // dataArr[11-…]
            ip = ByteArrayToIpMapper(dataArr), // dataArr[4-7]
            port = ByteArrayToPortMapper(dataArr), // dataArr[8-9]
        )
    }
}
