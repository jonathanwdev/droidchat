package com.jonathan.droidchat.data.network

import com.jonathan.droidchat.model.NetworkException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.bodyAsText

suspend fun <T> handleNetworkException(
    block: suspend () -> T
): T {
    return try {
        block()
    } catch(e: ClientRequestException) {
        val error = e.response.bodyAsText()
        throw NetworkException.ApiException(error, e.response.status.value)
    } catch (e: Exception) {
        throw NetworkException.UnknownNetworkException(e)
    }
}