package ua.frist008.action.record.data.network

import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.BoundDatagramSocket
import io.ktor.network.sockets.InetSocketAddress
import io.ktor.network.sockets.aSocket
import kotlinx.coroutines.delay
import timber.log.Timber
import java.net.BindException
import kotlin.time.Duration.Companion.milliseconds

/** Binds the UDP sockets of the protocol, retrying while a port is held, as the original app */
object DatagramSocketBinder {

    /**
     * Binds a UDP socket on every address of the phone.
     *
     * @param selectorManager the selector of the sockets of the process.
     * @param port the local port to receive on.
     * @param isBroadcast whether the socket sends to a broadcast address.
     * @return the bound socket, which the caller closes.
     * @throws BindException when the port stays held after every attempt.
     */
    suspend fun bind(
        selectorManager: SelectorManager,
        port: Int,
        isBroadcast: Boolean,
    ): BoundDatagramSocket {
        val bindAttempts = 5
        val bindRetryDelay = 500.milliseconds
        val localAddress = InetSocketAddress(NetworkConst.ANY_HOST, port)

        repeat(bindAttempts - 1) { attempt ->
            try {
                return aSocket(selectorManager).udp().bind(localAddress) { broadcast = isBroadcast }
            } catch (e: BindException) {
                Timber.w(e, "$port; attempt $attempt")
                delay(bindRetryDelay)
            }
        }

        return aSocket(selectorManager).udp().bind(localAddress) { broadcast = isBroadcast }
    }
}
