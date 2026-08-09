package ua.frist008.action.record

import androidx.activity.ComponentActivity
import dagger.hilt.android.AndroidEntryPoint

// Minimal activity for @HiltAndroidTest instrumented tests.
// Declared in src/debug/AndroidManifest.xml so it is only bundled in debug builds.
@AndroidEntryPoint
class HiltTestActivity : ComponentActivity()
