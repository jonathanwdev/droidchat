package com.jonathan.droidchat.data.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jonathan.droidchat.data.mapper.toDomainModel
import com.jonathan.droidchat.data.network.NetworkDataSource
import com.jonathan.droidchat.data.network.model.PaginationParams
import com.jonathan.droidchat.model.User
import javax.inject.Inject

class UserPagingSource @Inject constructor(
    private val networkDataSource: NetworkDataSource
) : PagingSource<Int, User>() {
    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, User> {
       return try {

            val offset = params.key ?: 0
            val response = networkDataSource.getUsers(
                paginationParams = PaginationParams(
                    offset = offset.toString(),
                    limit = params.loadSize.toString()
                )
            )
            val users = response.users.map { it.toDomainModel() }

            return LoadResult.Page(
                data = users,
                prevKey = null,
                    nextKey = if(response.hasMore) {
                        offset + params.loadSize
                    }else {
                        null
                    }
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(state.config.pageSize) ?: anchorPage?.nextKey?.minus(state.config.pageSize)
        }
    }
}