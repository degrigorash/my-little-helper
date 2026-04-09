package com.grig.danish.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.grig.danish.data.model.Noun
import kotlinx.coroutines.flow.Flow

@Dao
interface NounDao {

    @Query("SELECT * FROM nouns")
    fun getAll(): Flow<List<Noun>>

    @Query("SELECT * FROM nouns WHERE folder = :folder")
    fun getByFolder(folder: String): Flow<List<Noun>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(nouns: List<Noun>)
}
