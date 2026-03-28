package com.grig.myanimelist.ui.mangasearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grig.myanimelist.data.MalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
            _state.value = MangaSearchState.Idle(query = query)
            return
        }
        _state.value = MangaSearchState.Searching(query = query)
        searchJob = viewModelScope.launch {
            delay(400)
            val result = malRepository.searchManga(query)
            result.fold(
                onSuccess = { list ->
                    val results = list.data.map { node -> node.manga }
                    if (results.isEmpty()) {
                        _state.value = MangaSearchState.NoResults(query = query)
                    } else {
                        _state.value = MangaSearchState.Results(query = query, results = results)
                    }
                },
                onFailure = { error ->
                    _state.value = MangaSearchState.Error(
                        query = query,
                        message = error.message ?: "Search failed"
                    )
                }
            )
        }
    }
}
