package com.grig.myanimelist.ui.mangasearch

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
class MangaSearchViewModel @Inject constructor(
    private val malRepository: MalRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MangaSearchState())
    val state: StateFlow<MangaSearchState> = _state.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChange(query: String) {
        _state.update { it.copy(query = query, selectedManga = null, error = null) }
        searchJob?.cancel()
        if (query.length < 3) {
            _state.update { it.copy(searchResults = emptyList(), isSearching = false) }
            return
        }
        _state.update { it.copy(isSearching = true, searchResults = emptyList()) }
        searchJob = viewModelScope.launch {
            delay(400)
            val result = malRepository.searchManga(query)
            result.fold(
                onSuccess = { list ->
                    _state.update {
                        it.copy(
                            searchResults = list.data.map { node -> node.manga },
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

    fun onMangaSelected(mangaId: Int) {
        _state.update {
            it.copy(
                searchResults = emptyList(),
                isLoadingDetail = true,
                error = null,
                relatedAnime = emptyList(),
                isLoadingRelatedAnime = false
            )
        }
        viewModelScope.launch {
            val result = malRepository.getMangaDetails(mangaId)
            result.fold(
                onSuccess = { manga ->
                    _state.update { it.copy(selectedManga = manga, isLoadingDetail = false) }
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
        loadRelatedAnime(mangaId)
    }

    private fun loadRelatedAnime(mangaId: Int) {
        _state.update { it.copy(isLoadingRelatedAnime = true) }
        viewModelScope.launch {
            val relations = malRepository.getMangaRelatedAnime(mangaId)
            _state.update { it.copy(relatedAnime = relations, isLoadingRelatedAnime = false) }
        }
    }

    fun addToMyList() {
        val mangaId = _state.value.selectedManga?.id ?: return
        _state.update { it.copy(isUpdatingList = true, error = null) }
        viewModelScope.launch {
            val result = malRepository.updateMangaListStatus(
                mangaId = mangaId,
                status = "plan_to_read"
            )
            result.fold(
                onSuccess = { refreshDetail(mangaId) },
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
        val mangaId = _state.value.selectedManga?.id ?: return
        _state.update { it.copy(isUpdatingList = true, error = null) }
        viewModelScope.launch {
            val result = malRepository.deleteMangaListItem(mangaId)
            result.fold(
                onSuccess = { refreshDetail(mangaId) },
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

    private suspend fun refreshDetail(mangaId: Int) {
        val result = malRepository.getMangaDetails(mangaId)
        result.fold(
            onSuccess = { manga ->
                _state.update {
                    it.copy(selectedManga = manga, isUpdatingList = false, listChanged = true)
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
