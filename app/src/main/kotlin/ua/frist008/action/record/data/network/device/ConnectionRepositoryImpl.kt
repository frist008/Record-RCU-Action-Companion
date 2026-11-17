package ua.frist008.action.record.data.network.device

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ua.frist008.action.record.data.local.device.DeviceDAO
import ua.frist008.action.record.features.connection.ConnectionRepository
import ua.frist008.action.record.features.connection.entity.DeviceDomainEntity
import javax.inject.Inject

class ConnectionRepositoryImpl @Inject constructor(
    private val deviceDAO: DeviceDAO,
    private val deviceRadarNetworkSource: DeviceRadarNetworkSource,
    @param:ApplicationContext private val context: Context,
) : ConnectionRepository {

    // The name is a constant copied into the app; only Android 17 and newer are asked for it
    // (isLocalNetworkGranted)
    @SuppressLint("InlinedApi")
    override val localNetworkPermission: String = Manifest.permission.ACCESS_LOCAL_NETWORK

    override fun get(): Flow<DeviceDomainEntity?> =
        deviceRadarNetworkSource.get().map { dtoList ->
            val lastDbo = deviceDAO.get()
            val dto = dtoList.firstOrNull { it.ip == lastDbo?.ip } ?: dtoList.firstOrNull()
            val dbo = dto?.toDBO()

            if (dbo != null && dbo != lastDbo) {
                deviceDAO.insert(dbo)
            }

            dto?.toDomain()
        }

    override fun isLocalNetworkGranted(): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.CINNAMON_BUN ||
            ContextCompat.checkSelfPermission(context, localNetworkPermission) ==
            PackageManager.PERMISSION_GRANTED
}
