package com.jonathan.droidchat.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.jonathan.droidchat.data.di.IoDispatcher
import com.jonathan.droidchat.data.mapper.toDomainModel
import com.jonathan.droidchat.data.network.NetworkDataSource
import com.jonathan.droidchat.data.pagingsource.UserPagingSource
import com.jonathan.droidchat.data.util.safeCallResult
import com.jonathan.droidchat.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : UserRepository {
    override fun getUsers(limit: Int): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(
                pageSize = limit,
                prefetchDistance = 1,
                initialLoadSize = limit,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                UserPagingSource(networkDataSource)
            }
        ).flow
    }

    override suspend fun getUser(userId: Int): Result<User> {
        return safeCallResult(ioDispatcher) {
            val userResponse  = networkDataSource.getUser(userId)
            userResponse.toDomainModel()
        }
    }
}