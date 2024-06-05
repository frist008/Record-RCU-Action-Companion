package ua.frist008.action.record.util.exception

import timber.log.Timber
import ua.frist008.action.record.BuildConfig

fun unsupportedUI() {
    if (BuildConfig.DEBUG) {
        throw UnsupportedOperationException()
    } else {
        Timber.e(UnsupportedOperationException())
    }
}
