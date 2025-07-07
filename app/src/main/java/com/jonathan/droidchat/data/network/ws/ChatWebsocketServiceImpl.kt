package com.jonathan.droidchat.data.network.ws

import android.util.Log
import com.jonathan.droidchat.data.network.model.MessageResponse
import com.jonathan.droidchat.data.network.model.MessageSocketDataRequest
import com.jonathan.droidchat.data.network.model.MessageSocketRequest
import com.jonathan.droidchat.model.ActiveUserIdsResponse
import com.jonathan.droidchat.model.WebSocketData
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.sendSerialized
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.serialization.json.Json
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

class ChatWebsocketServiceImpl @Inject constructor(
    private val client: HttpClient
) : ChatWebSocketService {
    private val tag = "***ChatWebsocketService***"

    private var socketSession: DefaultClientWebSocketSession? = null
    private val socketUrl = "ws://chat-api.androidmoderno.com.br:8080/chat/"

    override suspend fun connect(userId: Int) {
        if (socketSession != null) {
            Log.w(tag, "Already connected. Skiping new connection attempt")
            return
        }

        try {
            socketSession = client.webSocketSession {
                url("$socketUrl$userId")
            }
            if (socketSession?.isActive == true) {
                Log.w(tag, "Connected to websocket successfully")
            } else {
                Log.w(tag, "Failed to connect to websocket")
            }
        } catch (err: Exception) {
            Log.w(tag, "Websocket connection failed: $err")

        }
    }

    override suspend fun disconnect() {
        socketSession?.close()
        socketSession = null
        Log.w(tag, "Websocket disconnected")
    }

    override fun observeSocketMessageResultFlow(): Flow<SocketMessageResult> {
        if (socketSession == null) {
            Log.w(tag, "Websocket session is null. Cannot observe messages")
            flowOf(SocketMessageResult.ConnectionError(Throwable("Websocket session is null")))
        }
        return socketSession!!
            .incoming
            .receiveAsFlow()
            .filterIsInstance(Frame.Text::class)
            .map { frame ->
                val text = frame.readText()
                val webSocketData = Json.decodeFromString<WebSocketData>(text)
                Log.w(tag, "Received data: $webSocketData")
                when (val data = webSocketData.data) {
                    is MessageResponse -> SocketMessageResult.MessageReceived(data)
                    is ActiveUserIdsResponse -> SocketMessageResult.ActiveUsersChanged(data)
                    else -> SocketMessageResult.NotHandledYet
                }
            }.catch { e ->
                Log.w(tag, "Websocket error: $e")
                flowOf(SocketMessageResult.ConnectionError(e))
            }

    }

    override suspend fun sendMessage(receiverId: Int, message: String) {
        if (socketSession == null || socketSession?.isActive == false) {
            Log.w(tag, "Websocket session is null or not active. Cannot send message")
            throw IllegalArgumentException("Websocket session is null or not active")
        }
        try {
            Log.w(tag, "Sending message: $message")
            val messageRequest = MessageSocketDataRequest(
                type = "messageRequest",
                data = MessageSocketRequest(
                    messageId = UUID.randomUUID().toString(),
                    receiverId = receiverId,
                    text = message,
                    timestamp = Instant.now().toEpochMilli()
                )
            )
            socketSession?.sendSerialized(messageRequest)

        } catch (err: Exception) {
            Log.w(tag, "Websocket sendMessage error: $err")
            throw err
        }
    }
}