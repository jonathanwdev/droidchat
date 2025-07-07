package com.jonathan.droidchat.data.network

import com.jonathan.droidchat.data.network.model.AuthRequest
import com.jonathan.droidchat.data.network.model.CreateAccountRequest
import com.jonathan.droidchat.data.network.model.ImageResponse
import com.jonathan.droidchat.data.network.model.PaginatedChatResponse
import com.jonathan.droidchat.data.network.model.PaginatedMessageResponse
import com.jonathan.droidchat.data.network.model.PaginatedUserResponse
import com.jonathan.droidchat.data.network.model.PaginationParams
import com.jonathan.droidchat.data.network.model.TokenResponse
import com.jonathan.droidchat.data.network.model.UserResponse
import com.jonathan.droidchat.model.RegisterTokenRequest

interface NetworkDataSource {
    suspend fun signUp(request: CreateAccountRequest)
    suspend fun signIn(request: AuthRequest): TokenResponse
    suspend fun uploadProfilePicture(filePath: String): ImageResponse
    suspend fun authenticate(): UserResponse
    suspend fun registerNotificationToken(registerTokenRequest: RegisterTokenRequest)
    suspend fun getChats(paginationParams: PaginationParams): PaginatedChatResponse
    suspend fun getUsers(paginationParams: PaginationParams): PaginatedUserResponse
    suspend fun getUser(userId: Int): UserResponse
    suspend fun getMessages(receiverId: Int, paginationParams: PaginationParams): PaginatedMessageResponse

}