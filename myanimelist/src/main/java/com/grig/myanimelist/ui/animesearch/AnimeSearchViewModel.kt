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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeSearchViewModel @Inject constructor(
    private val malRepository: MalRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AnimeSearchState())
    val state: StateFlow<AnimeSearchState> = _state.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChange(query: String) {
        _state.update { it.copy(query = query, selectedAnime = null, error = null) }
        searchJob?.cancel()
        if (query.length < 3) {
            _state.update { it.copy(searchResults = emptyList(), isSearching = false) }
            return
        }
        _state.update { it.copy(isSearching = true, searchResults = emptyList()) }
        searchJob = viewModelScope.launch {
            delay(400)
            val result = malRepository.searchAnime(query)
            result.fold(
                onSuccess = { list ->
                    _state.update {
                        it.copy(
                            searchResults = list.data.map { node -> node.anime },
                            isSearching = false
                        )
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            isSearching = false,
                            error = error.message ?: "Search failed"
                        )
                    }
                }
            )
        }
    }

    fun onAnimeSelected(animeId: Int) {
        _state.update {
            it.copy(
                searchResults = emptyList(),
                isLoadingDetail = true,
                error = null
            )
        }
        viewModelScope.launch {
            val result = malRepository.getAnimeDetails(animeId)
            result.fold(
                onSuccess = { anime ->
                    _state.update { it.copy(selectedAnime = anime, isLoadingDetail = false) }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            isLoadingDetail = false,
                            error = error.message ?: "Failed to load details"
                        )
                    }
                }
            )
        }
    }

    fun addToMyList() {
        val animeId = _state.value.selectedAnime?.id ?: return
        _state.update { it.copy(isUpdatingList = true, error = null) }
        viewModelScope.launch {
            val result = malRepository.updateAnimeListStatus(
                animeId = animeId,
                status = "plan_to_watch"
            )
            result.fold(
                onSuccess = { refreshDetail(animeId) },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            isUpdatingList = false,
                            error = error.message ?: "Failed to add"
                        )
                    }
                }
            )
        }
    }

    fun deleteFromMyList() {
        val animeId = _state.value.selectedAnime?.id ?: return
        _state.update { it.copy(isUpdatingList = true, error = null) }
        viewModelScope.launch {
            val result = malRepository.deleteAnimeListItem(animeId)
            result.fold(
                onSuccess = { refreshDetail(animeId) },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            isUpdatingList = false,
                            error = error.message ?: "Failed to delete"
                        )
                    }
                }
            )
        }
    }

    private suspend fun refreshDetail(animeId: Int) {
        val result = malRepository.getAnimeDetails(animeId)
        result.fold(
            onSuccess = { anime ->
                _state.update {
                    it.copy(selectedAnime = anime, isUpdatingList = false, listChanged = true)
                }
            },
            onFailure = {
                _state.update { it.copy(isUpdatingList = false) }
            }
        )
    }

    suspend fun isAuthorized(): Boolean {
        return malRepository.userFlow.first() is MalUserState.Authorized
    }
}
