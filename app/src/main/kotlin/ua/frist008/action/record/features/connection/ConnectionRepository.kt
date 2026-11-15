package ua.frist008.action.record.features.connection

import kotlinx.coroutines.flow.Flow
import ua.frist008.action.record.features.connection.entity.DeviceDomainEntity

interface ConnectionRepository {

    /**
     * Searches the network for the PC to connect to and remembers it as the last one: the PC the
     * phone connected to last when it replied, else the first that replied.
     *
     * @return the PC chosen in each period of the search, or null when no PC replied in it.
     */
    fun get(): Flow<DeviceDomainEntity?>
}