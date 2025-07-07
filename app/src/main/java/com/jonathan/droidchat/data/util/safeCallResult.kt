package com.jonathan.droidchat.data.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

suspend fun <T>safeCallResult(
    dispatcher: CoroutineDispatcher,
    block: suspend () -> T
): Result<T> {
    return withContext(dispatcher) {
        runCatching {
            block()
        }
    }
}