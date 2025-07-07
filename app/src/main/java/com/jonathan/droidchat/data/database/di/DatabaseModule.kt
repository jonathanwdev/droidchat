package com.jonathan.droidchat.data.database.di

import android.content.Context
import androidx.room.Room
import com.jonathan.droidchat.data.database.DroidChatDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(
        @ApplicationContext
        context: Context
    ): DroidChatDatabase {
        return Room.databaseBuilder(
            context,
            DroidChatDatabase::class.java,
            "droidchat.db"
        ).build()
    }
}