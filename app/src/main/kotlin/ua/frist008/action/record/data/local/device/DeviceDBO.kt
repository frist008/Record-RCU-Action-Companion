package ua.frist008.action.record.data.local.device

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ua.frist008.action.record.data.mapper.toIPStr
import ua.frist008.action.record.data.network.device.entity.DeviceDTO
import ua.frist008.action.record.features.device.entity.DeviceDomainEntity

@Entity(
    tableName = DeviceDBO.NAME_TABLE,
    indices = [Index(value = [DeviceDBO.IP], unique = true)],
)
data class DeviceDBO(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    val id: Long = 0,
    @ColumnInfo(name = NAME)
    val name: String?,
    @ColumnInfo(name = IP, index = true)
    val ip: Long,
    @ColumnInfo(name = PORT)
    val port: Int,
) {

    fun toDomain(isAvailable: Boolean): DeviceDomainEntity =
        DeviceDomainEntity(
            id = id,
            isAvailableStatus = isAvailable,
            name = name,
            address = "${ip.toIPStr}:$port",
        )

    fun toDTO(): DeviceDTO =
        DeviceDTO(
            name = name,
            ip = ip,
            port = port,
        )

    companion object {
        const val NAME_TABLE = "Devices"
        const val ID = "id_$NAME_TABLE"
        const val NAME = "name_$NAME_TABLE"
        const val IP = "ip_$NAME_TABLE"
        const val PORT = "port_$NAME_TABLE"
    }
}
