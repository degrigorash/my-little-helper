package com.grig.myanimelist.ui.characters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.grig.myanimelist.MalRoute
import com.grig.myanimelist.data.MalRepository
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

    init {
        loadCharacters()
    }

    private fun loadCharacters() {
        _state.value = CharactersState.Loading
        viewModelScope.launch {
            val result = when (mediaType) {
                CharactersMediaType.ANIME -> malRepository.getAnimeCharacters(mediaId)
                CharactersMediaType.MANGA -> malRepository.getMangaCharacters(mediaId)
            }
            result.fold(
                onSuccess = { response ->
                    if (response.data.isEmpty()) {
                        _state.value = CharactersState.Empty
                    } else {
                        _state.value = CharactersState.Content(characters = response.data)
                    }
                },
                onFailure = { error ->
                    _state.value = CharactersState.Error(
                        message = error.message ?: "Failed to load characters"
                    )
                }
            )
        }
    }
}
