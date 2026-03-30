package com.grig.danish.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.grig.danish.data.model.Noun

@Database(
    entities = [Noun::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(StringListConverter::class)
abstract class DanishDatabase : RoomDatabase() {
    abstract fun nounDao(): NounDao
}
