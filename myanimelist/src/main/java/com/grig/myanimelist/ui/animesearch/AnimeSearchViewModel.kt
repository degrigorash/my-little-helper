package com.grig.myanimelist.ui.animesearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grig.myanimelist.data.MalRepository
import com.grig.myanimelist.data.model.MalUserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeSearchViewModel @Inject constructor(
    private val malRepository: MalRepository
) : ViewModel() {

    private val _state = MutableStateFlow<AnimeSearchState>(AnimeSearchState.Idle())
    val state: StateFlow<AnimeSearchState> = _state.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChange(query: String) {
        searchJob?.cancel()
        if (query.length < 3) {
            _state.value = AnimeSearchState.Idle(query = query, listChanged = _state.value.listChanged)
            return
        }
        _state.value = AnimeSearchState.Searching(query = query, listChanged = _state.value.listChanged)
        searchJob = viewModelScope.launch {
            delay(400)
            val result = malRepository.searchAnime(query)
            result.fold(
                onSuccess = { list ->
                    val results = list.data.map { node -> node.anime }
                    if (results.isEmpty()) {
                        _state.value = AnimeSearchState.NoResults(query = query, listChanged = _state.value.listChanged)
                    } else {
                        _state.value = AnimeSearchState.Results(query = query, listChanged = _state.value.listChanged, results = results)
                    }
                },
                onFailure = { error ->
                    _state.value = AnimeSearchState.Error(
                        query = query,
                        listChanged = _state.value.listChanged,
                        message = error.message ?: "Search failed"
                    )
                }
            )
        }
    }

    fun onAnimeSelected(animeId: Int) {
        val current = _state.value
        _state.value = AnimeSearchState.LoadingDetail(query = current.query, listChanged = current.listChanged)
        viewModelScope.launch {
            val result = malRepository.getAnimeDetails(animeId)
            result.fold(
                onSuccess = { anime ->
                    _state.value = AnimeSearchState.Detail(
                        query = current.query,
                        listChanged = current.listChanged,
                        anime = anime
                    )
                },
                onFailure = { error ->
                    _state.value = AnimeSearchState.Error(
                        query = current.query,
                        listChanged = current.listChanged,
                        message = error.message ?: "Failed to load details"
                    )
                }
            )
        }
        loadRelatedManga(animeId)
    }

    private fun loadRelatedManga(animeId: Int) {
        viewModelScope.launch {
            val relations = malRepository.getAnimeRelatedManga(animeId)
            val current = _state.value
            if (current is AnimeSearchState.Detail) {
                _state.value = current.copy(relatedManga = relations, isLoadingRelatedManga = false)
            }
        }
    }

    fun addToMyList() {
        val current = _state.value as? AnimeSearchState.Detail ?: return
        val animeId = current.anime.id
        _state.value = current.copy(isUpdatingList = true)
        viewModelScope.launch {
            val result = malRepository.updateAnimeListStatus(
                animeId = animeId,
                status = "plan_to_watch"
            )
            result.fold(
                onSuccess = { refreshDetail(animeId) },
                onFailure = { error ->
                    val state = _state.value as? AnimeSearchState.Detail ?: return@fold
                    _state.value = state.copy(isUpdatingList = false)
                }
            )
        }
    }

    fun deleteFromMyList() {
        val current = _state.value as? AnimeSearchState.Detail ?: return
        val animeId = current.anime.id
        _state.value = current.copy(isUpdatingList = true)
        viewModelScope.launch {
            val result = malRepository.deleteAnimeListItem(animeId)
            result.fold(
                onSuccess = { refreshDetail(animeId) },
                onFailure = { error ->
                    val state = _state.value as? AnimeSearchState.Detail ?: return@fold
                    _state.value = state.copy(isUpdatingList = false)
                }
            )
        }
    }

    private suspend fun refreshDetail(animeId: Int) {
        val result = malRepository.getAnimeDetails(animeId)
        result.fold(
            onSuccess = { anime ->
                val current = _state.value as? AnimeSearchState.Detail
                _state.value = AnimeSearchState.Detail(
                    query = _state.value.query,
                    listChanged = true,
                    anime = anime,
                    isUpdatingList = false,
                    relatedManga = current?.relatedManga ?: emptyList(),
                    isLoadingRelatedManga = false
                )
            },
            onFailure = {
                val current = _state.value as? AnimeSearchState.Detail ?: return
                _state.value = current.copy(isUpdatingList = false)
            }
        )
    }

    suspend fun isAuthorized(): Boolean {
        return malRepository.userFlow.first() is MalUserState.Authorized
    }
}
