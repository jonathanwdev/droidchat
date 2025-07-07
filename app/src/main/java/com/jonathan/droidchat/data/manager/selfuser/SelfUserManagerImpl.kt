package com.jonathan.droidchat.data.manager.selfuser

import android.content.Context
import com.jonathan.droidchat.SelfUser
import com.jonathan.droidchat.data.dataStore.selfUserStore
import com.jonathan.droidchat.data.di.IoDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SelfUserManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): SelfUserManager {
    private val selfUserStore = context.selfUserStore

    override val selfUserFlow: Flow<SelfUser>
        get() = selfUserStore.data

    override suspend fun saveSelfUserData(
        id: Int,
        firstName: String,
        lastName: String,
        profilePictureUrl: String,
        username: String
    ) {
        withContext(ioDispatcher) {
            selfUserStore.updateData {
                it.toBuilder()
                    .setId(id)
                    .setFirstName(firstName)
                    .setLastName(lastName)
                    .setProfilePictureUrl(profilePictureUrl)
                    .setUsername(username)
                    .build()
            }
        }
    }

    override suspend fun clearSelfUser() {
        withContext(ioDispatcher) {
            selfUserStore.updateData {
                it.toBuilder()
                    .clearId()
                    .clearFirstName()
                    .clearLastName()
                    .clearProfilePictureUrl()
                    .clearUsername()
                    .build()
            }
        }
    }
}