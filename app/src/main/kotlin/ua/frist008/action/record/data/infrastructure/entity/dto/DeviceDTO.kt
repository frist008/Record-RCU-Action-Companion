package ua.frist008.action.record.data.infrastructure.entity.dto

import ua.frist008.action.record.data.infrastructure.entity.dbo.DeviceDBO
import ua.frist008.action.record.data.infrastructure.mapper.IpLongToStringMapper

data class DeviceDTO(
    val name: String?,
    val ip: Long,
    val port: Int,
) {

    val ipStr by lazy { IpLongToStringMapper(ip) }

    fun toDBO() = DeviceDBO(name = name, ip = ip, port = port)
}
