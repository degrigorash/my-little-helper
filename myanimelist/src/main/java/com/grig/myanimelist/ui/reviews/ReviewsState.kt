package com.grig.myanimelist.ui.reviews

import com.grig.myanimelist.data.model.jikan.JikanReview

enum class ReviewsMediaType(val value: String) {
    ANIME("anime"),
    MANGA("manga");

    companion object {
        fun fromValue(value: String) = entries.first { it.value == value }
    }
}

sealed interface ReviewsState {
    data object Loading : ReviewsState
    data object Empty : ReviewsState
    data class Content(
        val reviews: List<JikanReview>,
        val isLoadingMore: Boolean = false,
        val hasNextPage: Boolean = false,
        val currentPage: Int = 1
    ) : ReviewsState
    data class Error(val message: String) : ReviewsState
}
