package com.jonathan.droidchat.data.repository

import androidx.paging.PagingData
import com.jonathan.droidchat.data.network.ws.SocketMessageResult
import com.jonathan.droidchat.model.Chat
import com.jonathan.droidchat.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    val newMessageReceivedFlow: Flow<Unit>

    suspend fun getChats(offset: Int, limit: Int): Result<List<Chat>>
    fun getPagedMessages(receiverId: Int): Flow<PagingData<ChatMessage>>
    suspend fun sendMessage(receiverId: Int, message: String): Result<Unit>
    suspend fun connectWebsocket(): Result<Unit>
    suspend fun disconnectWebsocket()
    fun observeSocketMessageResultFlow(): Flow<SocketMessageResult>
}