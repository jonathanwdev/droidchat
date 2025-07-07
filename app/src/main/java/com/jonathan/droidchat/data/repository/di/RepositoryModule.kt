package com.jonathan.droidchat.data.repository.di

import com.jonathan.droidchat.data.repository.AuthRepository
import com.jonathan.droidchat.data.repository.AuthRepositoryImp
import com.jonathan.droidchat.data.repository.ChatRepository
import com.jonathan.droidchat.data.repository.ChatRepositoryImpl
import com.jonathan.droidchat.data.repository.UserRepository
import com.jonathan.droidchat.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {

    @Binds
    fun bindsAuthRepository(
        repository: AuthRepositoryImp
    ): AuthRepository

    @Binds
    fun bindsChatRepository(
        repository: ChatRepositoryImpl
    ): ChatRepository

    @Binds
    fun bindsUserRepository(
        repository: UserRepositoryImpl
    ): UserRepository

}