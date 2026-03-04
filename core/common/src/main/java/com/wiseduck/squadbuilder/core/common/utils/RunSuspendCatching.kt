package com.wiseduck.squadbuilder.core.common.utils

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
suspend inline fun <R> runSuspendCatching(crossinline block: suspend () -> R): Result<R> {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    return try {
        Result.success(block())
    } catch (e: TimeoutCancellationException) {
        Result.failure(e)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Result.failure(e)
    }
}
