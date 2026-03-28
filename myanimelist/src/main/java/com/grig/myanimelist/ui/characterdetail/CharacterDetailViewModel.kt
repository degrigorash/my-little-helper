package com.grig.myanimelist.ui.characterdetail

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
class CharacterDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val malRepository: MalRepository
) : ViewModel() {

    private val route = savedStateHandle.toRoute<MalRoute.CharacterDetail>()
    private val characterId: Int = route.characterId

    private val _state = MutableStateFlow<CharacterDetailState>(CharacterDetailState.Loading)
    val state: StateFlow<CharacterDetailState> = _state.asStateFlow()

    init {
        loadCharacter()
    }

    private fun loadCharacter() {
        _state.value = CharacterDetailState.Loading
        viewModelScope.launch {
            malRepository.getCharacterFull(characterId).fold(
                onSuccess = { response ->
                    _state.value = CharacterDetailState.Content(character = response.data)
                },
                onFailure = { error ->
                    _state.value = CharacterDetailState.Error(
                        message = error.message ?: "Failed to load character"
                    )
                }
            )
        }
    }
}
