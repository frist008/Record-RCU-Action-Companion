package ua.frist008.action.record.core.presentation

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.updateAndGet
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import ua.frist008.action.record.core.presentation.dependency.PresentationDependenciesDelegate
import ua.frist008.action.record.core.presentation.dependency.StateOwner
import ua.frist008.action.record.core.ui.UIState
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class BaseViewModel(
    dependencies: PresentationDependenciesDelegate,
) : ViewModel(CoroutineScope(dependencies.backgroundDispatcher + SupervisorJob())),
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

    protected val mutableState = MutableStateFlow<UIState>(UIState.Progress())
    override val state by lazy(LazyThreadSafetyMode.NONE) { mutableState.asStateFlow() }

    protected fun launch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit,
    ): Job = viewModelScope.launch(context + coroutineExceptionHandler, start, block)

    protected fun launch(
        jobController: AtomicRef<Job?>,
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit,
    ): Job? = jobController.updateAndGet { job ->
        job?.cancel()

        this.launch(context, start) {
            job?.join()
            block()
        }
    }

    @CallSuper protected open suspend fun onFailure(cause: Throwable) {
        Timber.e(cause)
    }
}
