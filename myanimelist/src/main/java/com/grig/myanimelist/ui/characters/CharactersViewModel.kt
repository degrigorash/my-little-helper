package com.grig.myanimelist.ui.characters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.grig.myanimelist.MalRoute
import com.grig.myanimelist.data.MalRepository
import com.grig.myanimelist.data.model.jikan.JikanCharacterEntry
import com.grig.myanimelist.data.toJikanErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val malRepository: MalRepository
) : ViewModel() {

    private val route = savedStateHandle.toRoute<MalRoute.Characters>()
    private val mediaId: Int = route.mediaId
    private val mediaType: CharactersMediaType = CharactersMediaType.fromValue(route.mediaType)

    private val _state = MutableStateFlow<CharactersState>(CharactersState.Loading)
    val state: StateFlow<CharactersState> = _state.asStateFlow()

    private var cached: List<JikanCharacterEntry> = emptyList()
    private var searchQuery: String = ""

    init {
        loadCharacters()
    }

    fun retry() {
        loadCharacters()
    }

    fun onSearchQueryChange(query: String) {
        searchQuery = query
        applyFilter()
    }

    private fun loadCharacters() {
        _state.value = CharactersState.Loading
        searchQuery = ""
        viewModelScope.launch {
            val result = when (mediaType) {
                CharactersMediaType.ANIME -> malRepository.getAnimeCharacters(mediaId)
                CharactersMediaType.MANGA -> malRepository.getMangaCharacters(mediaId)
            }
            result.fold(
                onSuccess = { response ->
                    if (response.data.isEmpty()) {
                        cached = emptyList()
                        _state.value = CharactersState.Empty
                    } else {
                        cached = response.data.sortedWith(
                            compareBy<JikanCharacterEntry> { it.role.roleSortOrder() }
                                .thenByDescending { it.favorites }
                        )
                        applyFilter()
                    }
                },
                onFailure = { error ->
                    _state.value = CharactersState.Error(
                        message = error.toJikanErrorMessage("Failed to load characters")
                    )
                }
            )
        }
    }

    private fun applyFilter() {
        if (cached.isEmpty()) return
        val query = searchQuery
        val filtered = if (query.isBlank()) {
            cached
        } else {
            cached.filter { entry ->
                entry.character.name.contains(query, ignoreCase = true) ||
                    entry.voiceActors.any { it.person.name.contains(query, ignoreCase = true) }
            }
        }
        _state.value = CharactersState.Content(
            characters = filtered,
            searchQuery = query
        )
    }
}

private fun String.roleSortOrder(): Int = when (lowercase()) {
    "main" -> 0
    "supporting" -> 1
    else -> 2
}
