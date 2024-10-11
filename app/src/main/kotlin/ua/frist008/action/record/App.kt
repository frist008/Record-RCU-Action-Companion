package ua.frist008.action.record

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.crashlytics.crashlytics
import com.google.firebase.perf.performance
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import ua.frist008.action.record.core.util.logger.CrashlyticsTree
import ua.frist008.action.record.core.util.logger.TagDebugTree

@HiltAndroidApp class App : Application() {

    override fun onCreate() {
        super.onCreate()

        setupTimber()
        initFirebase()
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(CrashlyticsTree(), TagDebugTree())
        } else {
            Timber.plant(CrashlyticsTree())
        }

        Timber.i("Application.onCreate")
    }

    private fun initFirebase() {
        Firebase.performance.isPerformanceCollectionEnabled = !BuildConfig.DEBUG

        Firebase.analytics.setAnalyticsCollectionEnabled(!BuildConfig.DEBUG)

        Firebase.crashlytics.isCrashlyticsCollectionEnabled = !BuildConfig.DEBUG
        Firebase.crashlytics.setCustomKey("DEBUG", BuildConfig.DEBUG)
    }
}
