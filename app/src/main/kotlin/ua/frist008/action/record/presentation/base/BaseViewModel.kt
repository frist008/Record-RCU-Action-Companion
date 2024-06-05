package ua.frist008.action.record.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import ua.frist008.action.record.presentation.base.entity.PresentationDependenciesDelegate
import ua.frist008.action.record.ui.entity.base.UIEntity
import ua.frist008.action.record.ui.entity.base.UIState
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel<State : UIState<out UIEntity, out Exception>>(
    dependencies: PresentationDependenciesDelegate,
) : ViewModel(),
    PresentationDependenciesDelegate by dependencies {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, cause ->
        runCatching { onFailure(cause) }
            .onSuccess { Timber.i(cause) }
            .onFailure { innterCause -> Timber.e(cause, innterCause.stackTraceToString()) }
    }

    protected val scope by lazy(LazyThreadSafetyMode.NONE) { viewModelScope }

    protected abstract val mutableState: MutableStateFlow<State>
    val state by lazy(LazyThreadSafetyMode.NONE) { mutableState.asStateFlow() }

    protected fun launch(
        context: CoroutineContext = backgroundDispatcher,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit,
    ): Job =
        scope.launch(context + coroutineExceptionHandler, start, block)

    protected abstract fun onFailure(cause: Throwable)
}
