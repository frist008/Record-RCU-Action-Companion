package ua.frist008.action.record.data.local.device

import ua.frist008.action.record.data.network.device.entity.DeviceDTO

/**
 * The PC the phone connected to last, as it is kept between launches.
 *
 * @property name the name the PC sent, if any.
 * @property ip the IPv4 address of the PC.
 * @property port the TCP port Action! listens on.
 */
data class DeviceDBO(
    val name: String?,
    val ip: Long,
    val port: Int,
) {

    fun toDTO(): DeviceDTO =
        DeviceDTO(
            name = name,
            ip = ip,
            port = port,
        )
}