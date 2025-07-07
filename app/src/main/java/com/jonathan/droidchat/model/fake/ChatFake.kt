package com.jonathan.droidchat.model.fake

import com.jonathan.droidchat.model.Chat
import com.jonathan.droidchat.model.User

val chat1 = Chat(
    id = 1,
    lastMessage = "ok, lets go !!",
    unreadCount = 1,
    timestamp = "12:22",
    members = listOf(
        user1,
        user2

    )
)

val chat2 = Chat(
    id = 1,
    lastMessage = "That's it !",
    unreadCount = 0,
    timestamp = "12:22",
    members = listOf(
        user4,
        user2

    )
)

val chat3 = Chat(
    id = 1,
    lastMessage = "Hello World",
    unreadCount = 2,
    timestamp = "12:22",
    members = listOf(
        user3,
        user2

    )
)