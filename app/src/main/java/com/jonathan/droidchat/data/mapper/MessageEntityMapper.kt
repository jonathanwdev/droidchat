package com.jonathan.droidchat.data.mapper

import com.jonathan.droidchat.data.database.entity.MessageEntity
import com.jonathan.droidchat.model.ChatMessage

fun MessageEntity.toDomainModel(selfUserId: Int?): ChatMessage {
    return ChatMessage(
        id = this.id,
        senderId = this.senderId,
        receiverId = this.receiverId,
        autoId = this.autoId,
        text = this.text,
        formattedDateTime = this.timestamp.toTimestamp(),
        isUnread = this.isUnread,
        isSelf =  this.senderId == selfUserId
    )
}