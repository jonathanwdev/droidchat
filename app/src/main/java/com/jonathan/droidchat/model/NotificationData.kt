package com.jonathan.droidchat.model

import kotlinx.serialization.Serializable


@Serializable
data class NotificationData(
    val userId: Int,
    val userName: String,
    val profileImageUrl: String,
    val message: String,
    val action: String,
)
