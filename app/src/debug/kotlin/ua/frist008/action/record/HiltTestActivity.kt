package ua.frist008.action.record

import androidx.activity.ComponentActivity
import dagger.hilt.android.AndroidEntryPoint

// Minimal activity for @HiltAndroidTest instrumented tests.
// Lives in the debug source set beside its declaration in src/debug/AndroidManifest.xml,
// so it is bundled in debug builds only.
@AndroidEntryPoint
class HiltTestActivity : ComponentActivity()
