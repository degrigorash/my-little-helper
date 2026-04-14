package com.grig.myanimelist.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {

    @Query("SELECT * FROM watchlist ORDER BY addedAt DESC")
    fun getAll(): Flow<List<WatchlistEntry>>

    @Query("SELECT animeId FROM watchlist")
    fun getAllIds(): Flow<List<Int>>

    @Query("SELECT * FROM watchlist WHERE animeId = :animeId LIMIT 1")
    suspend fun getByAnimeId(animeId: Int): WatchlistEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: WatchlistEntry)

    @Query("DELETE FROM watchlist WHERE animeId = :animeId")
    suspend fun delete(animeId: Int)
}
