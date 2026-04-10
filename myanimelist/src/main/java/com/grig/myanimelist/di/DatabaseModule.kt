package com.grig.myanimelist.di

import android.content.Context
import androidx.room.Room
import com.grig.myanimelist.data.local.MalDatabase
import com.grig.myanimelist.data.local.WatchlistDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideMalDatabase(@ApplicationContext context: Context): MalDatabase {
        return Room.databaseBuilder(
            context,
            MalDatabase::class.java,
            "mal_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideWatchlistDao(database: MalDatabase): WatchlistDao = database.watchlistDao()
}
