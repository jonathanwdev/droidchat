package com.jonathan.droidchat.data.database

import androidx.paging.PagingSource
import com.jonathan.droidchat.data.database.entity.MessageEntity
import com.jonathan.droidchat.data.database.entity.MessageRemoteKeyEntity

interface DatabaseDataSource {
    suspend fun insertMessages(messages: List<MessageEntity>)
    suspend fun clearMessages(receiverId: Int)
    fun getPagedMessages(receiverId: Int): PagingSource<Int, MessageEntity>

    suspend fun getMessageRemoteKey(receiverId: Int): MessageRemoteKeyEntity?
    suspend fun insertMessageRemoteKey(remoteKey: MessageRemoteKeyEntity)
    suspend fun clearMessageRemoteKey(receiverId: Int)

}