package com.jonathan.droidchat.model

import com.jonathan.droidchat.data.network.serializer.WebSocketDataSerializer
import kotlinx.serialization.Serializable

@Serializable(with = WebSocketDataSerializer::class)
data class WebSocketData(
    val type: String,
    val data: Any
)
