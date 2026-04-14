package com.grig.myanimelist.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [WatchlistEntry::class],
    version = 1,
    exportSchema = true
)
abstract class MalDatabase : RoomDatabase() {
    abstract fun watchlistDao(): WatchlistDao
}
