package com.jonathan.droidchat.data.mapper

import com.jonathan.droidchat.data.database.entity.MessageEntity
import com.jonathan.droidchat.data.network.model.PaginatedMessageResponse

fun PaginatedMessageResponse.toEntityModel(): List<MessageEntity> = this.messages.map {
    MessageEntity(
        id = it.id,
        text = it.text,
        isUnread = it.isUnread,
        senderId = it.senderId,
        receiverId = it.receiverId,
        timestamp = it.timestamp
    )
}