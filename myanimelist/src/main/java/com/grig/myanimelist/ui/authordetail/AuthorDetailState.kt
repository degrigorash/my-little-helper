package com.grig.myanimelist.ui.authordetail

import com.grig.myanimelist.data.model.jikan.JikanPersonFull

sealed interface AuthorDetailState {
    data object Loading : AuthorDetailState
    data class Content(val person: JikanPersonFull) : AuthorDetailState
    data class Error(val message: String) : AuthorDetailState
}
