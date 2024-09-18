package ua.frist008.action.record.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import ua.frist008.action.record.core.presentation.dependency.PresentationDependenciesDelegate
import ua.frist008.action.record.core.presentation.dependency.StateOwner
import ua.frist008.action.record.core.ui.UIState
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(
    dependencies: PresentationDependenciesDelegate,
) : ViewModel(),
    PresentationDependenciesDelegate by dependencies,
    StateOwner {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, cause ->
        runCatching { if (cause !is CancellationException) launch { onFailure(cause) } }
            .onSuccess { Timber.i(cause) }
            .onFailure { innerCause ->
                if (cause !is CancellationException) {
                    Timber.e(innerCause.stackTraceToString(), cause)
                }
            }
    }

    protected val scope by lazy(LazyThreadSafetyMode.NONE) { viewModelScope }

    protected val mutableState = MutableStateFlow<UIState>(UIState.Progress())
    override val state by lazy(LazyThreadSafetyMode.NONE) { mutableState.asStateFlow() }

    protected fun BaseViewModel.launch(
        context: CoroutineContext = backgroundDispatcher,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit,
    ): Job = scope.launch(context + coroutineExceptionHandler, start, block)

    protected abstract suspend fun onFailure(cause: Throwable)
}
