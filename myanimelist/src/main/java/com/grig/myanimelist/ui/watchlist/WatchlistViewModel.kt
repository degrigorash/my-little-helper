package com.grig.myanimelist.ui.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grig.myanimelist.data.MalRepository
import com.grig.myanimelist.data.local.WatchlistDao
import com.grig.myanimelist.data.model.anime.MalAnimeListStatus
import com.grig.myanimelist.ui.animelist.AnimeCardData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val watchlistDao: WatchlistDao,
    private val malRepository: MalRepository
) : ViewModel() {

    private val _state = MutableStateFlow<WatchlistState>(WatchlistState.Loading)
    val state: StateFlow<WatchlistState> = _state.asStateFlow()

    private val _editSheetAnime = MutableStateFlow<AnimeCardData?>(null)
    val editSheetAnime: StateFlow<AnimeCardData?> = _editSheetAnime.asStateFlow()

    init {
        loadWatchlist()
    }

    fun loadWatchlist() {
        viewModelScope.launch {
            _state.value = WatchlistState.Loading
            val entries = watchlistDao.getAll().first()
            if (entries.isEmpty()) {
                _state.value = WatchlistState.Empty
                return@launch
            }

            val items = entries.map { entry ->
                async {
                    val result = malRepository.getAnimeDetails(entry.animeId)
                    result.getOrNull()?.let { anime ->
                        WatchlistItemData(
                            anime = anime,
                            listStatus = anime.myListStatus,
                            notes = anime.myListStatus?.comments
                        )
                    }
                }
            }.awaitAll().filterNotNull()

            _state.value = if (items.isEmpty()) {
                WatchlistState.Empty
            } else {
                WatchlistState.Content(items)
            }
        }
    }

    fun onItemClick(item: WatchlistItemData) {
        _editSheetAnime.value = AnimeCardData(item.anime, item.listStatus)
    }

    fun dismissEditSheet() {
        _editSheetAnime.value = null
    }

    fun onAnimeUpdated(animeId: Int, newStatus: MalAnimeListStatus) {
        _editSheetAnime.value = null
        loadWatchlist()
    }

    fun onAnimeDeleted(animeId: Int) {
        _editSheetAnime.value = null
        viewModelScope.launch {
            watchlistDao.delete(animeId)
        }
        loadWatchlist()
    }
}
