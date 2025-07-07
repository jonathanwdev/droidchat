package com.jonathan.droidchat.data.network.ws

import kotlinx.coroutines.flow.Flow

interface ChatWebSocketService {
    suspend fun connect(userId: Int)
    suspend fun disconnect()
    fun observeSocketMessageResultFlow(): Flow<SocketMessageResult>
    suspend fun sendMessage(receiverId: Int, message: String)
}