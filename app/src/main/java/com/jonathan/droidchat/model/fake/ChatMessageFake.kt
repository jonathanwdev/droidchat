package com.jonathan.droidchat.model.fake

import com.jonathan.droidchat.model.ChatMessage

val chatMessage1 = ChatMessage(
    autoId = 1,
    text = "Olá",
    id = 1,
    isSelf = true,
    formattedDateTime = "15:00",
    isUnread = true,
    senderId = 1,
    receiverId = 2
)
val chatMessage2 = ChatMessage(
    autoId = 2,
    text = "Tudo bem?",
    id = 2,
    isSelf = false,
    formattedDateTime = "15:01",
    isUnread = true,
    senderId = 2,
    receiverId = 1
)

val chatMessage3 = ChatMessage(
    autoId = 3,
    text = "Sim, tudo bem",
    id = 3,
    isSelf = true,
    formattedDateTime = "15:02",
    isUnread = true,
    senderId = 1,
    receiverId = 2
)
val chatMessage4 = ChatMessage(
    autoId = 4,
    text = "Que bom!",
    id = 4,
    isSelf = false,
    formattedDateTime = "15:03",
    isUnread = true,
    senderId = 2,
    receiverId = 1
)
val chatMessage5 = ChatMessage(
    autoId = 5,
    text = "E você?",
    id = 5,
    isSelf = true,
    formattedDateTime = "15:04",
    isUnread = true,
    senderId = 1,
    receiverId = 2
)

