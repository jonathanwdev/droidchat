package com.jonathan.droidchat.data.network.ws

import com.jonathan.droidchat.data.network.model.MessageResponse
import com.jonathan.droidchat.model.ActiveUserIdsResponse

sealed interface  SocketMessageResult{
    data object NotHandledYet: SocketMessageResult
    data class MessageReceived(val message: MessageResponse): SocketMessageResult
    data class ActiveUsersChanged(val activeUsersIdsResponse: ActiveUserIdsResponse): SocketMessageResult
    data class ConnectionError(val error: Throwable): SocketMessageResult
}