package com.grig.myanimelist.ui.characters

import com.grig.myanimelist.data.model.jikan.JikanCharacterEntry

enum class CharactersMediaType(val value: String) {
    ANIME("anime"),
    MANGA("manga");

    companion object {
        fun fromValue(value: String) = entries.first { it.value == value }
    }
}

sealed interface CharactersState {
    data object Loading : CharactersState
    data object Empty : CharactersState
    data class Content(val characters: List<JikanCharacterEntry>) : CharactersState
    data class Error(val message: String) : CharactersState
}
