package com.grig.myanimelist.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlist")
data class WatchlistEntry(
    @PrimaryKey val animeId: Int,
    val addedAt: Long = System.currentTimeMillis()
)
