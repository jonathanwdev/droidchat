package com.jonathan.droidchat.data.manager.notification

interface NotificationManager {
    suspend fun getToken(): String
}