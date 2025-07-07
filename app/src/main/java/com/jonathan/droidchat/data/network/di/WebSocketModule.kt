package com.jonathan.droidchat.data.network.di

import com.jonathan.droidchat.data.network.ws.ChatWebSocketService
import com.jonathan.droidchat.data.network.ws.ChatWebsocketServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface WebSocketModule {

    @Binds
    @Singleton
    fun bindWebsocketService(chatWebsocketService: ChatWebsocketServiceImpl): ChatWebSocketService
}