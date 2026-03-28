package com.grig.myanimelist.ui.animesearch

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
class AnimeSearchViewModel @Inject constructor(
    private val malRepository: MalRepository
) : ViewModel() {

    private val _state = MutableStateFlow<AnimeSearchState>(AnimeSearchState.Idle())
    val state: StateFlow<AnimeSearchState> = _state.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChange(query: String) {
        searchJob?.cancel()
        if (query.length < 3) {
            _state.value = AnimeSearchState.Idle(query = query)
            return
        }
        _state.value = AnimeSearchState.Searching(query = query)
        searchJob = viewModelScope.launch {
            delay(400)
            val result = malRepository.searchAnime(query)
            result.fold(
                onSuccess = { list ->
                    val results = list.data.map { node -> node.anime }
                    if (results.isEmpty()) {
                        _state.value = AnimeSearchState.NoResults(query = query)
                    } else {
                        _state.value = AnimeSearchState.Results(query = query, results = results)
                    }
                },
                onFailure = { error ->
                    _state.value = AnimeSearchState.Error(
                        query = query,
                        message = error.message ?: "Search failed"
                    )
                }
            )
        }
    }
}
