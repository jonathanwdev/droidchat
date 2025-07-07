package com.jonathan.droidchat.service

import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.jonathan.droidchat.DroidChatApplication
import com.jonathan.droidchat.MainActivity
import com.jonathan.droidchat.R
import com.jonathan.droidchat.data.manager.ChatNotificationManager
import com.jonathan.droidchat.data.manager.selfuser.SelfUserManager
import com.jonathan.droidchat.data.util.NotificationPayloadParse
import com.jonathan.droidchat.model.NotificationData
import com.jonathan.droidchat.navigation.CHAT_BASE_DETAIL_URI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FcmMessageService : FirebaseMessagingService() {

    @Inject
    lateinit var selfUserManager: SelfUserManager

    @Inject
    lateinit var chatNotificationManager: ChatNotificationManager


    @Inject
    lateinit var notificationPayloadParse: NotificationPayloadParse

    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onNewToken(token: String) {
        super.onNewToken(token)

    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        scope.launch {
            val selfUser = selfUserManager.selfUserFlow.firstOrNull()
            if (selfUser?.id != null) {
                val notificationPayloadJsonString = message.data["messagePayload"]
                notificationPayloadJsonString?.let { payload ->
                    val notification = notificationPayloadParse.parse(payload)
                    if(DroidChatApplication.onAppForeground) {
                        chatNotificationManager.notifyNewMessage(notification)
                        return@launch
                    }
                    sendNotification(notification)
                }
            }
        }
    }

    private fun sendNotification(notification: NotificationData) {
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            action = Intent.ACTION_VIEW
            data = "${CHAT_BASE_DETAIL_URI}/${notification.userId}".toUri()
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat
            .Builder(applicationContext, DroidChatApplication.CHAT_MESSAGES_CHANNEL_ID)
            .setContentTitle(notification.userName)
            .setContentText(notification.message)
            .setSmallIcon(R.drawable.logo)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(applicationContext)

        notificationManager.notify(notification.userId, notificationBuilder.build())
    }
}