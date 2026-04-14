package com.grig.danish.di

import android.content.Context
import androidx.room.Room
import com.grig.danish.data.local.DanishDatabase
import com.grig.danish.data.local.NounDao
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
    fun provideDanishDatabase(@ApplicationContext context: Context): DanishDatabase {
        return Room.databaseBuilder(
            context,
            DanishDatabase::class.java,
            "danish_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideNounDao(database: DanishDatabase): NounDao = database.nounDao()
}
