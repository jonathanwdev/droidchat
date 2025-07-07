package com.jonathan.droidchat.data.mapper

import com.jonathan.droidchat.SelfUser
import com.jonathan.droidchat.model.User

fun SelfUser.toDomainModel() = User(
    id = this.id,
    self = true,
    firstName = this.firstName,
    lastName = this.lastName,
    profilePictureUrl =this. profilePictureUrl,
    username = this.username
)