package ua.frist008.action.record

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

// Swaps the app Application for HiltTestApplication so @HiltAndroidTest can inject.
// Hilt ships no ready-made runner, so it has to be declared as testInstrumentationRunner.
class HiltTestRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application =
        super.newApplication(cl, HiltTestApplication::class.java.name, context)
}
