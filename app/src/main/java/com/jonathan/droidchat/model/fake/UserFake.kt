package com.jonathan.droidchat.model.fake

import com.jonathan.droidchat.model.User

val user1 = User(
    id = 1,
    firstName = "John",
    lastName = "Doe",
    username = "John.doe",
    profilePictureUrl = null,
    self = false
)

val user2 = User(
    id = 2,
    firstName = "Jonathan",
    lastName = "Doe",
    username = "Jonathan.doe",
    profilePictureUrl = null,
    self = true
)

val user3 = User(
    id = 3,
    firstName = "Carl",
    lastName = "Lucky",
    username = "Carl.lucky",
    profilePictureUrl = null,
    self = false
)

val user4 = User(
    id = 4,
    firstName = "Marie",
    lastName = "Ann",
    username = "Marie.ann",
    profilePictureUrl = null,
    self = false
)