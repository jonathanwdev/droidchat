package com.jonathan.droidchat.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jonathan.droidchat.data.database.entity.MessageEntity

@Dao
interface MessageDao {
    @Query("""
    SELECT * FROM messages 
    WHERE (receiver_id = :receiverId OR sender_id = :receiverId )
    ORDER BY timestamp DESC
    """)
    fun getMessages(receiverId: Int): PagingSource<Int, MessageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(message: List<MessageEntity>)

    @Query("DELETE FROM messages WHERE receiver_id = :receiverId OR sender_id = :receiverId")
    suspend fun clearMessages(receiverId: Int)
}