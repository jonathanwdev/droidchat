package com.jonathan.droidchat.model

data class CreateAccount(
    val firstName: String,
    val lastName: String,
    val username: String,
    val password: String,
    val profilePictureId: Int?
)