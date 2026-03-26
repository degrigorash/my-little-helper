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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MangaSearchViewModel @Inject constructor(
    private val malRepository: MalRepository
) : ViewModel() {

    private val _state = MutableStateFlow<MangaSearchState>(MangaSearchState.Idle())
    val state: StateFlow<MangaSearchState> = _state.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChange(query: String) {
        searchJob?.cancel()
        if (query.length < 3) {
            _state.value = MangaSearchState.Idle(query = query, listChanged = _state.value.listChanged)
            return
        }
        _state.value = MangaSearchState.Searching(query = query, listChanged = _state.value.listChanged)
        searchJob = viewModelScope.launch {
            delay(400)
            val result = malRepository.searchManga(query)
            result.fold(
                onSuccess = { list ->
                    val results = list.data.map { node -> node.manga }
                    if (results.isEmpty()) {
                        _state.value = MangaSearchState.NoResults(query = query, listChanged = _state.value.listChanged)
                    } else {
                        _state.value = MangaSearchState.Results(query = query, listChanged = _state.value.listChanged, results = results)
                    }
                },
                onFailure = { error ->
                    _state.value = MangaSearchState.Error(
                        query = query,
                        listChanged = _state.value.listChanged,
                        message = error.message ?: "Search failed"
                    )
                }
            )
        }
    }

    fun onMangaSelected(mangaId: Int) {
        val current = _state.value
        _state.value = MangaSearchState.LoadingDetail(query = current.query, listChanged = current.listChanged)
        viewModelScope.launch {
            val result = malRepository.getMangaDetails(mangaId)
            result.fold(
                onSuccess = { manga ->
                    _state.value = MangaSearchState.Detail(
                        query = current.query,
                        listChanged = current.listChanged,
                        manga = manga
                    )
                },
                onFailure = { error ->
                    _state.value = MangaSearchState.Error(
                        query = current.query,
                        listChanged = current.listChanged,
                        message = error.message ?: "Failed to load details"
                    )
                }
            )
        }
        loadRelatedAnime(mangaId)
    }

    private fun loadRelatedAnime(mangaId: Int) {
        viewModelScope.launch {
            val relations = malRepository.getMangaRelatedAnime(mangaId)
            val current = _state.value
            if (current is MangaSearchState.Detail) {
                _state.value = current.copy(relatedAnime = relations, isLoadingRelatedAnime = false)
            }
        }
    }

    fun addToMyList() {
        val current = _state.value as? MangaSearchState.Detail ?: return
        val mangaId = current.manga.id
        _state.value = current.copy(isUpdatingList = true)
        viewModelScope.launch {
            val result = malRepository.updateMangaListStatus(
                mangaId = mangaId,
                status = "plan_to_read"
            )
            result.fold(
                onSuccess = { refreshDetail(mangaId) },
                onFailure = { error ->
                    val state = _state.value as? MangaSearchState.Detail ?: return@fold
                    _state.value = state.copy(isUpdatingList = false)
                }
            )
        }
    }

    fun deleteFromMyList() {
        val current = _state.value as? MangaSearchState.Detail ?: return
        val mangaId = current.manga.id
        _state.value = current.copy(isUpdatingList = true)
        viewModelScope.launch {
            val result = malRepository.deleteMangaListItem(mangaId)
            result.fold(
                onSuccess = { refreshDetail(mangaId) },
                onFailure = { error ->
                    val state = _state.value as? MangaSearchState.Detail ?: return@fold
                    _state.value = state.copy(isUpdatingList = false)
                }
            )
        }
    }

    private suspend fun refreshDetail(mangaId: Int) {
        val result = malRepository.getMangaDetails(mangaId)
        result.fold(
            onSuccess = { manga ->
                val current = _state.value as? MangaSearchState.Detail
                _state.value = MangaSearchState.Detail(
                    query = _state.value.query,
                    listChanged = true,
                    manga = manga,
                    isUpdatingList = false,
                    relatedAnime = current?.relatedAnime ?: emptyList(),
                    isLoadingRelatedAnime = false
                )
            },
            onFailure = {
                val current = _state.value as? MangaSearchState.Detail ?: return
                _state.value = current.copy(isUpdatingList = false)
            }
        )
    }

    suspend fun isAuthorized(): Boolean {
        return malRepository.userFlow.first() is MalUserState.Authorized
    }
}
