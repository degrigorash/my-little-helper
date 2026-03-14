package com.grig.myanimelist.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.grig.myanimelist.data.model.anime.MalAnimeWatchingStatus
import com.grig.myanimelist.data.model.manga.MalMangaReadingStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

class FilterPreferencesManager @Inject constructor(
    @param:Named("FilterDataStore")
    private val dataStore: DataStore<Preferences>
) {

    suspend fun loadAnimeFilter(): Set<MalAnimeWatchingStatus> {
        val names = dataStore.data.map { it[ANIME_FILTER] ?: emptySet() }.first()
        return names.mapNotNull { name ->
            MalAnimeWatchingStatus.entries.find { it.apiValue == name }
        }.toSet()
    }

    suspend fun loadMangaFilter(): Set<MalMangaReadingStatus> {
        val names = dataStore.data.map { it[MANGA_FILTER] ?: emptySet() }.first()
        return names.mapNotNull { name ->
            MalMangaReadingStatus.entries.find { it.apiValue == name }
        }.toSet()
    }

    suspend fun saveAnimeFilter(filter: Set<MalAnimeWatchingStatus>) {
        dataStore.edit { prefs ->
            prefs[ANIME_FILTER] = filter.map { it.apiValue }.toSet()
        }
    }

    suspend fun saveMangaFilter(filter: Set<MalMangaReadingStatus>) {
        dataStore.edit { prefs ->
            prefs[MANGA_FILTER] = filter.map { it.apiValue }.toSet()
        }
    }

    companion object {
        private val ANIME_FILTER = stringSetPreferencesKey("anime_filter")
        private val MANGA_FILTER = stringSetPreferencesKey("manga_filter")
    }
}
