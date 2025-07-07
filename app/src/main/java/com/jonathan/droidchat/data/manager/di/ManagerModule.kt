package com.jonathan.droidchat.data.manager.di

import com.jonathan.droidchat.data.manager.notification.NotificationManager
import com.jonathan.droidchat.data.manager.notification.NotificationManagerImpl
import com.jonathan.droidchat.data.manager.selfuser.SelfUserManager
import com.jonathan.droidchat.data.manager.selfuser.SelfUserManagerImpl
import com.jonathan.droidchat.data.manager.token.SecureTokenManagerImpl
import com.jonathan.droidchat.data.manager.token.TokenManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ManagerModule {
    @Binds
    @Singleton
    fun bindTokenManager(impl: SecureTokenManagerImpl): TokenManager

    @Binds
    @Singleton
    fun bindSelfUserManager(impl: SelfUserManagerImpl): SelfUserManager

    @Binds
    @Singleton
    fun bindNotificationManager(impl: NotificationManagerImpl): NotificationManager

}