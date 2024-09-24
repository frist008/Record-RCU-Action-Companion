package ua.frist008.action.record.analytics

import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapNotNull
import timber.log.Timber
import ua.frist008.action.record.core.util.common.takeIfNotEmpty
import ua.frist008.action.record.features.record.entity.RecordSuccessState

object Analytics {

    private val analytics by lazy(LazyThreadSafetyMode.NONE) { Firebase.analytics }

    private const val RECORD_DATA_EVENT = "Record Data"

    private val engineFlow = MutableStateFlow<String?>(null)

    private suspend fun logEngine(engine: String) {
        engineFlow.emit(engine)
    }

    suspend fun subscribe() {
        engineFlow.mapNotNull { it.takeIfNotEmpty() }.collect { engine ->
            Timber.tag("Engine").d(engine)
            analytics.logEvent(RECORD_DATA_EVENT) { param("Engine", engine.replace(' ', '_')) }
        }
    }

    suspend fun log(state: RecordSuccessState) {
        if (state.engine.name.isNotEmpty()) {
            logEngine(state.engine.name)
        }
    }
}
