package com.grig.myanimelist.ui.characterdetail

import com.grig.myanimelist.data.model.jikan.JikanCharacterFull

sealed interface CharacterDetailState {
    data object Loading : CharacterDetailState
    data class Content(val character: JikanCharacterFull) : CharacterDetailState
    data class Error(val message: String) : CharacterDetailState
}
