package com.jonathan.droidchat.data.manager.notification

import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import com.jonathan.droidchat.data.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NotificationManagerImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): NotificationManager {

    private val firebaseMessaging: FirebaseMessaging by lazy {
        Firebase.messaging
    }

    override suspend fun getToken(): String {
        return withContext(ioDispatcher) {
            firebaseMessaging.token.await()
        }
    }
}