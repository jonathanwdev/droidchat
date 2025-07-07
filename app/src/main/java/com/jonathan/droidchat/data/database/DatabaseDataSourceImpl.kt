package com.jonathan.droidchat.data.database

import androidx.paging.PagingSource
import com.jonathan.droidchat.data.database.entity.MessageEntity
import com.jonathan.droidchat.data.database.entity.MessageRemoteKeyEntity
import javax.inject.Inject

class DatabaseDataSourceImpl @Inject constructor(
    database: DroidChatDatabase
): DatabaseDataSource {
    private val dbMessages = database.messageDao()
    private val dbMessagesRemoteKey = database.messageRemoteKeyDao()

    override suspend fun insertMessages(messages: List<MessageEntity>) {
        dbMessages.insertMessages(messages)
    }

    override suspend fun clearMessages(receiverId: Int) {
        dbMessages.clearMessages(receiverId)
    }

    override fun getPagedMessages(receiverId: Int): PagingSource<Int, MessageEntity> {
        return dbMessages.getMessages(receiverId)
    }

    override suspend fun getMessageRemoteKey(receiverId: Int): MessageRemoteKeyEntity? {
        return dbMessagesRemoteKey.getRemoteKey(receiverId)
    }

    override suspend fun insertMessageRemoteKey(remoteKey: MessageRemoteKeyEntity) {
        dbMessagesRemoteKey.insertRemoteKey(remoteKey)
    }

    override suspend fun clearMessageRemoteKey(receiverId: Int) {
        dbMessagesRemoteKey.clearRemoteKey(receiverId)
    }
}