package ua.frist008.action.record.core.util.logger

import timber.log.Timber

class TagDebugTree : Timber.DebugTree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(
            priority,
            "Timber.${tag ?: "NULL"}",
            message,
            t?.let(TagDebugTree::DebugException),
        )
    }

    private class DebugException(cause: Throwable) : Throwable(cause)
}
