package ua.frist008.action.record.data.network.device

import kotlinx.coroutines.flow.Flow
import ua.frist008.action.record.data.network.device.entity.DeviceDTO

interface DeviceRadarNetworkSource {

    /**
     * Scans the network while collected, opening its socket on collection and closing it on
     * cancellation.
     *
     * @return the PCs that replied within each scan period, once per period, and right away when a
     * new PC replies.
     */
    fun get(): Flow<List<DeviceDTO>>
}
