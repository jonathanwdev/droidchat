package com.jonathan.droidchat.data.mapper

import com.jonathan.droidchat.data.network.model.UserResponse
import com.jonathan.droidchat.model.User

fun UserResponse.toDomainModel(): User = User(
    id = this.id,
    firstName = this.firstName,
    lastName = this.lastName,
    self = false,
    username = this.username,
    profilePictureUrl = this.profilePictureUrl
)


