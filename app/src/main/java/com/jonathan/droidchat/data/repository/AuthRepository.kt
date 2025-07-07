package com.jonathan.droidchat.data.repository

import com.jonathan.droidchat.model.CreateAccount
import com.jonathan.droidchat.model.Image
import com.jonathan.droidchat.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUserFlow: Flow<User>
    suspend fun getAccessToken(): String?
    suspend fun clearAccessToken()
    suspend fun signUp(request: CreateAccount): Result<Unit>
    suspend fun signIn(username: String, password: String):  Result<Unit>
    suspend fun uploadProfilePicture(filePath: String): Result<Image>
    suspend fun authenticate(): Result<Unit>

}