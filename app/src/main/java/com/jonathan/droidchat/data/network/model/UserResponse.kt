package com.jonathan.droidchat.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class PaginatedUserResponse(
    val users: List<UserResponse>,
    val hasMore: Boolean,
    val total: Int
)

@Serializable
data class UserResponse(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val username: String,
    val profilePictureUrl: String?,
)
