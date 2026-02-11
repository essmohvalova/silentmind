package com.example.coursework_app.utils

import kotlinx.coroutines.CancellationException

inline fun <R> runSuspendCatching(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (c: CancellationException) {
        throw c
    } catch (e: Throwable) {
        Result.failure(e)
    }
}

fun <T> Result<T>.onError(block: (Throwable) -> Unit): Result<T> {
    exceptionOrNull()?.let { block(it) }
    return this
}

suspend fun <T> withTimeoutSafe(
    timeMillis: Long,
    block: suspend () -> T
): Result<T> = runSuspendCatching {
    kotlinx.coroutines.withTimeout(timeMillis) { block() }
}