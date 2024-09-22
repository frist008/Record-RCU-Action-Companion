package ua.frist008.action.record.data.local.device

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

// TODO @Dao Will Impl at version 1.1
@Singleton class DeviceDAO @Inject constructor() {

    // TODO remove
    private var dbo: DeviceDBO? = null

    //    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dbo: DeviceDBO) {
        // TODO Will Impl at version 1.1
        this.dbo = dbo
    }

    //    @Query("SELECT * FROM ${DevicesDBO.NAME_TABLE} WHERE ${DeviceDBO.ID} = :deviceId")
    suspend fun get(deviceId: Long): DeviceDBO? {
        // TODO Will Impl at version 1.1
        return dbo
    }

    //    @Query("SELECT * FROM ${DevicesDBO.NAME_TABLE}")
    fun getAll(): Flow<List<DeviceDBO>> = MutableStateFlow(dbo?.let(::listOf) ?: emptyList())
}
