package com.jonathan.droidchat.data.util

import com.jonathan.droidchat.model.NotificationData
import kotlinx.serialization.json.Json
import javax.inject.Inject

class NotificationPayloadParse @Inject constructor() {
    fun parse(notificationPayloadJsonString: String): NotificationData {
        return Json.decodeFromString(notificationPayloadJsonString)
    }
}