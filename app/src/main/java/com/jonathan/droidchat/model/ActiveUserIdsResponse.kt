package com.jonathan.droidchat.model

import kotlinx.serialization.Serializable

@Serializable
data class ActiveUserIdsResponse(
    val activeUserIds: List<Int>
)
