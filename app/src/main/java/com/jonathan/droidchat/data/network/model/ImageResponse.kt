package com.jonathan.droidchat.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ImageResponse(
    val id: Int,
    val name: String,
    val url: String,
    val type: String,
)
