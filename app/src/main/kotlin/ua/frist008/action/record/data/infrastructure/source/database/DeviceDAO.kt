package ua.frist008.action.record.data.infrastructure.source.database

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ua.frist008.action.record.data.infrastructure.entity.dbo.DeviceDBO
import javax.inject.Inject

// TODO @Dao Will Impl at version 1.1
class DeviceDAO @Inject constructor() {

    // TODO remove
    private var dbo: DeviceDBO? = null

    //    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dbo: DeviceDBO) {
        // TODO Will Impl at version 1.1
        this.dbo = dbo
    }

    //    @Query("SELECT * FROM ${DevicesDBO.NAME_TABLE}")
    fun getAll(): Flow<List<DeviceDBO>> = MutableStateFlow(dbo?.let(::listOf) ?: emptyList())
}
