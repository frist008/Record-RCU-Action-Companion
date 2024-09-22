package ua.frist008.action.record.data.network.device.entity

import ua.frist008.action.record.data.local.device.DeviceDBO
import ua.frist008.action.record.data.mapper.IpLongToStringMapper

data class DeviceDTO(
    val name: String?,
    val ip: Long,
    val port: Int,
) {

    val ipStr by lazy { IpLongToStringMapper(ip) }

    fun toDBO() = DeviceDBO(name = name, ip = ip, port = port)
}
