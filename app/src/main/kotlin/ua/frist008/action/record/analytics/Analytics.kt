package ua.frist008.action.record.analytics

import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import kotlinx.atomicfu.atomic
import timber.log.Timber
import ua.frist008.action.record.features.record.entity.RecordSuccessState

object Analytics {

    private val analytics by lazy(LazyThreadSafetyMode.NONE) { Firebase.analytics }

    private const val RECORD_DATA_EVENT = "record_data"

    /** The engine of the last event, so the same engine is logged once while the process lives */
    private val lastEngine = atomic<String?>(null)

    fun log(state: RecordSuccessState) {
        val engine = state.engine.name.trim()
        val isNewEngine = engine.isNotEmpty() && lastEngine.getAndSet(engine) != engine

        if (isNewEngine) {
            Timber.tag("Engine").d(engine)
            analytics.logEvent(RECORD_DATA_EVENT) { param("engine", engine.replace(' ', '_')) }
        }
    }
}