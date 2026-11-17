package ua.frist008.action.record.data.network

import java.io.IOException

/** Thrown when a command is sent while no session with the PC is open */
class NotConnectedException : IOException("No session with the PC")
