package ua.frist008.action.record.util.extension.concurrent

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.onCompletion
import kotlin.coroutines.cancellation.CancellationException

fun <T> Flow<T>.onCancel(
    action: suspend FlowCollector<T>.() -> Unit,
): Flow<T> = onCompletion { cause -> if (cause is CancellationException) action() }
