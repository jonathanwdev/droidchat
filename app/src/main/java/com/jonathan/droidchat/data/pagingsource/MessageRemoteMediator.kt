package com.jonathan.droidchat.data.pagingsource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.jonathan.droidchat.data.database.DatabaseDataSource
import com.jonathan.droidchat.data.database.DroidChatDatabase
import com.jonathan.droidchat.data.database.entity.MessageEntity
import com.jonathan.droidchat.data.database.entity.MessageRemoteKeyEntity
import com.jonathan.droidchat.data.mapper.toEntityModel
import com.jonathan.droidchat.data.network.NetworkDataSource
import com.jonathan.droidchat.data.network.model.PaginationParams
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MessageRemoteMediator @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val databaseDataSource: DatabaseDataSource,
    private val droidChatDatabase: DroidChatDatabase,
    private val receiverId: Int
): RemoteMediator<Int, MessageEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MessageEntity>
    ): MediatorResult {
        return try {
            val offset = when(loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = databaseDataSource.getMessageRemoteKey(receiverId)
                    remoteKey?.nextOffset ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }
            val limit = state.config.pageSize
            val paginationParams = PaginationParams(
                offset = offset.toString(),
                limit = limit.toString()
            )
            val response = networkDataSource.getMessages(
                receiverId = receiverId,
                paginationParams = paginationParams
            )
            val entities = response.toEntityModel()
            droidChatDatabase.withTransaction {
                if(loadType == LoadType.REFRESH) {
                    databaseDataSource.clearMessages(receiverId)
                    databaseDataSource.clearMessageRemoteKey(receiverId)
                }

                databaseDataSource.insertMessageRemoteKey(
                   MessageRemoteKeyEntity(
                       receiverId = receiverId,
                       nextOffset = if(response.hasMore) offset + limit else null
                   )
                )

                databaseDataSource.insertMessages(
                    messages = entities
                )
            }

            MediatorResult.Success(endOfPaginationReached = !response.hasMore)
        }catch(err: Exception) {
            MediatorResult.Error(err)
        }
    }
}