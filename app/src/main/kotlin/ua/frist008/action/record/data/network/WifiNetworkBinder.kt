package ua.frist008.action.record.data.network

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Keeps the process on the Wi-Fi network while the mobile data is the default one, as it is when
 * the Wi-Fi has no internet, so the PC on the Wi-Fi stays in reach of the search and the session.
 */
@Singleton class WifiNetworkBinder @Inject constructor(
    private val connectivityManager: ConnectivityManager,
) {

    private val lock = Any()
    private var holderCount = 0

    private val wifiCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            synchronized(lock) {
                if (holderCount > 0) bindIfMobileDefault(network)
            }
        }

        override fun onLost(network: Network) {
            synchronized(lock) {
                if (network == connectivityManager.boundNetworkForProcess) unbindProcess()
            }
        }
    }

    /**
     * Runs the block with the process kept on the Wi-Fi network, the sockets it opens included.
     *
     * @param block the work with the PC, such as the search or the record session.
     * @return what the block returns.
     */
    suspend fun <T> withWifi(block: suspend () -> T): T {
        hold()

        try {
            return block()
        } finally {
            release()
        }
    }

    private fun hold() {
        synchronized(lock) {
            holderCount++

            if (holderCount == 1) {
                findWifiNetwork()?.let(::bindIfMobileDefault)
                connectivityManager.registerNetworkCallback(WIFI_REQUEST, wifiCallback)
            }
        }
    }

    private fun release() {
        synchronized(lock) {
            holderCount--

            if (holderCount == 0) {
                connectivityManager.unregisterNetworkCallback(wifiCallback)

                if (connectivityManager.boundNetworkForProcess != null) unbindProcess()
            }
        }
    }

    private fun bindIfMobileDefault(network: Network) {
        val defaultCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        val isMobileDefault =
            defaultCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true &&
                !defaultCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)

        if (isMobileDefault) {
            val isBound = connectivityManager.bindProcessToNetwork(network)

            Timber.i("bind to Wi-Fi: $isBound")
        }
    }

    private fun unbindProcess() {
        connectivityManager.bindProcessToNetwork(null)
        Timber.i("unbind from Wi-Fi")
    }

    /** Finds the Wi-Fi at once, as the callback answers after the caller has opened its sockets */
    @Suppress("DEPRECATION")
    private fun findWifiNetwork(): Network? =
        connectivityManager.allNetworks.firstOrNull { network ->
            connectivityManager.getNetworkCapabilities(network)
                ?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
        }

    companion object {

        private val WIFI_REQUEST = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()
    }
}