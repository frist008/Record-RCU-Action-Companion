package ua.frist008.action.record.data.infrastructure.entity.dbo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ua.frist008.action.record.domain.entity.DeviceDomainEntity

@Entity(
    tableName = DeviceDBO.NAME_TABLE,
    indices = [Index(value = [DeviceDBO.IP], unique = true)],
)
data class DeviceDBO(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    val id: Long = 0,
    @ColumnInfo(name = NAME)
    val name: String,
    @ColumnInfo(name = IP, index = true)
    val ip: String,
    @ColumnInfo(name = MAC)
    val mac: String,
) {

    fun toDomain(available: Boolean): DeviceDomainEntity =
        DeviceDomainEntity(
            id = id,
            availableStatus = available,
            name = name,
            address = "$ip:$mac",
        )

    companion object {
        const val NAME_TABLE = "Devices"
        const val ID = "id_$NAME_TABLE"
        const val NAME = "name_$NAME_TABLE"
        const val IP = "ip_$NAME_TABLE"
        const val MAC = "mac_$NAME_TABLE"
    }
}
