package com.jonathan.droidchat.model

data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val username: String,
    val profilePictureUrl: String?,
    val self: Boolean,
)
