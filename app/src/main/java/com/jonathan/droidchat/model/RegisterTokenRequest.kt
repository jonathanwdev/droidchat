package com.jonathan.droidchat.model

import kotlinx.serialization.Serializable


@Serializable
data class RegisterTokenRequest(
    val token: String
)
