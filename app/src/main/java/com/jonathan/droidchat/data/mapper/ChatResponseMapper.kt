package com.jonathan.droidchat.data.mapper

import com.jonathan.droidchat.data.network.model.PaginatedChatResponse
import com.jonathan.droidchat.model.Chat
import com.jonathan.droidchat.model.User

fun PaginatedChatResponse.toDomainModel(selfUserId: Int?): List<Chat> {
    return this.chats.map { chat ->
        Chat(
            id = chat.id,
            lastMessage = chat.lastMessage,
            unreadCount = chat.unreadCount,
            timestamp = chat.updatedAt.toTimestamp(),
            members = chat.members.map { member ->
                User(
                    id = member.id,
                    firstName = member.firstName,
                    lastName = member.lastName,
                    username = member.username,
                    profilePictureUrl = member.profilePictureUrl,
                    self = member.id == selfUserId,
                )
            }
        )

    }
}

