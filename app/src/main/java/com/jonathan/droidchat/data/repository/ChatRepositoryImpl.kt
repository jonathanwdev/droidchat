package com.jonathan.droidchat.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.jonathan.droidchat.data.database.DatabaseDataSource
import com.jonathan.droidchat.data.database.DroidChatDatabase
import com.jonathan.droidchat.data.database.entity.MessageEntity
import com.jonathan.droidchat.data.di.IoDispatcher
import com.jonathan.droidchat.data.manager.ChatNotificationManager
import com.jonathan.droidchat.data.manager.selfuser.SelfUserManager
import com.jonathan.droidchat.data.mapper.toDomainModel
import com.jonathan.droidchat.data.network.NetworkDataSource
import com.jonathan.droidchat.data.network.model.PaginationParams
import com.jonathan.droidchat.data.network.ws.ChatWebSocketService
import com.jonathan.droidchat.data.network.ws.SocketMessageResult
import com.jonathan.droidchat.data.pagingsource.MessageRemoteMediator
import com.jonathan.droidchat.data.util.safeCallResult
import com.jonathan.droidchat.model.Chat
import com.jonathan.droidchat.model.ChatMessage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val databaseDataSource: DatabaseDataSource,
    private val database: DroidChatDatabase,
    private val selfUserManager: SelfUserManager,
    private val chatNotificationManager: ChatNotificationManager,
    private val chatWebSocketService: ChatWebSocketService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ChatRepository {
    val user = runBlocking { selfUserManager.selfUserFlow.firstOrNull() }

    override val newMessageReceivedFlow: Flow<Unit>
        get() = chatNotificationManager.incomingMessageFlow.map {  }


    override suspend fun getChats(offset: Int, limit: Int): Result<List<Chat>> {
        return withContext(ioDispatcher) {
            runCatching {
                val response = networkDataSource.getChats(
                    PaginationParams(offset.toString(), limit.toString())
                )
                response.toDomainModel(user?.id)
            }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagedMessages(receiverId: Int): Flow<PagingData<ChatMessage>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20
            ),
            remoteMediator = MessageRemoteMediator(
                networkDataSource = networkDataSource,
                databaseDataSource = databaseDataSource,
                droidChatDatabase = database,
                receiverId = receiverId
            ),
            pagingSourceFactory = {
                databaseDataSource.getPagedMessages(receiverId)
            }
        ).flow.flowOn(ioDispatcher).map {
            it.map { messageEntity ->
                messageEntity.toDomainModel(user?.id)
            }
        }
    }

    override suspend fun sendMessage(receiverId: Int, message: String): Result<Unit> {
        return safeCallResult(ioDispatcher) {
            chatWebSocketService.sendMessage(receiverId, message)
        }
    }

    override suspend fun connectWebsocket(): Result<Unit> {
        return safeCallResult(ioDispatcher) {
            chatWebSocketService.connect(user?.id ?: 0)
        }
    }

    override suspend fun disconnectWebsocket() {
        withContext(ioDispatcher) {
            chatWebSocketService.disconnect()
        }
    }

    override fun observeSocketMessageResultFlow(): Flow<SocketMessageResult> {
        return chatWebSocketService.observeSocketMessageResultFlow()
            .onEach { socketResult ->
                when(socketResult) {
                    is SocketMessageResult.MessageReceived -> {
                        val messageEntity = MessageEntity(
                            id = socketResult.message.id,
                            senderId = socketResult.message.senderId,
                            receiverId = socketResult.message.receiverId,
                            text = socketResult.message.text,
                            isUnread = socketResult.message.isUnread,
                            timestamp = socketResult.message.timestamp
                        )
                        databaseDataSource.insertMessages(listOf(messageEntity))
                    }
                    else -> {

                    }
                }
            }.flowOn(ioDispatcher)
    }
}
