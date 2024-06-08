package ua.frist008.action.record.data.infrastructure.entity.dto

import ua.frist008.action.record.data.infrastructure.entity.dbo.DeviceDBO

data class DeviceDTO(
    val name: String,
    val ip: String,
    val mac: String,
) {

    fun toDBO() = DeviceDBO(name = name, ip = ip, mac = mac)
}
