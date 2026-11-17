package ua.frist008.action.record.features.connection

import kotlinx.coroutines.flow.Flow
import ua.frist008.action.record.features.connection.entity.DeviceDomainEntity

interface ConnectionRepository {

    /** The permission Android 17 and newer ask for before the search reaches the network */
    val localNetworkPermission: String

    /**
     * Searches the network for the PC to connect to and remembers it as the last one: the PC the
     * phone connected to last when it replied, else the first that replied.
     *
     * @return the PC chosen in each period of the search, or null when no PC replied in it.
     */
    fun get(): Flow<DeviceDomainEntity?>

    /**
     * Tells whether the search may reach the local network.
     *
     * @return true when the user gave [localNetworkPermission], and always below Android 17,
     * which asks for none.
     */
    fun isLocalNetworkGranted(): Boolean
}
