package com.jonathan.droidchat.data.repository

import androidx.paging.PagingData
import com.jonathan.droidchat.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUsers(limit: Int = 10): Flow<PagingData<User>>
    suspend fun getUser(userId: Int): Result<User>
}