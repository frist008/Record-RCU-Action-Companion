package ua.frist008.action.record.util.logger

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import timber.log.Timber

class CrashlyticsTree : Timber.Tree() {

    private val crashlytics by lazy { Firebase.crashlytics }

    override fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?,
    ) {
        if (priority == Log.ERROR) {
            crashlytics.recordException(Exception("${tag ?: "NULL"}: $message", t))
        }
    }
}
