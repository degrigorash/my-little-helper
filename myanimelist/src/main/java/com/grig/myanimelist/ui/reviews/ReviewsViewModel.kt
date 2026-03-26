package com.grig.myanimelist.ui.reviews

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.grig.myanimelist.MalRoute
import com.grig.myanimelist.data.MalRepository
import com.grig.myanimelist.data.model.jikan.JikanReview
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val malRepository: MalRepository
) : ViewModel() {

    private val route = savedStateHandle.toRoute<MalRoute.Reviews>()
    private val mediaId: Int = route.mediaId
    val mediaType: ReviewsMediaType = ReviewsMediaType.fromValue(route.mediaType)

    private val _state = MutableStateFlow<ReviewsState>(ReviewsState.Loading)
    val state: StateFlow<ReviewsState> = _state.asStateFlow()

    private val _activeFilter = MutableStateFlow<ReviewFilter?>(null)
    val activeFilter: StateFlow<ReviewFilter?> = _activeFilter.asStateFlow()

    init {
        loadReviews(page = 1)
    }

    fun toggleFilter(filter: ReviewFilter) {
        _activeFilter.value = if (_activeFilter.value == filter) null else filter
    }

    fun filteredReviews(reviews: List<JikanReview>): List<JikanReview> {
        val filter = _activeFilter.value ?: return reviews
        return reviews.filter { review -> filter.tag in review.tags }
    }

    private fun loadReviews(page: Int) {
        val isFirstPage = page == 1
        if (isFirstPage) {
            _state.value = ReviewsState.Loading
        } else {
            val current = _state.value as? ReviewsState.Content ?: return
            _state.value = current.copy(isLoadingMore = true)
        }
        viewModelScope.launch {
            val result = when (mediaType) {
                ReviewsMediaType.ANIME -> malRepository.getAnimeReviews(mediaId, page)
                ReviewsMediaType.MANGA -> malRepository.getMangaReviews(mediaId, page)
            }
            result.fold(
                onSuccess = { response ->
                    val existingReviews = (_state.value as? ReviewsState.Content)?.reviews ?: emptyList()
                    val allReviews = if (isFirstPage) response.data else existingReviews + response.data
                    if (allReviews.isEmpty()) {
                        _state.value = ReviewsState.Empty
                    } else {
                        _state.value = ReviewsState.Content(
                            reviews = allReviews,
                            isLoadingMore = false,
                            hasNextPage = response.pagination.hasNextPage,
                            currentPage = page
                        )
                    }
                },
                onFailure = { error ->
                    if (isFirstPage) {
                        _state.value = ReviewsState.Error(
                            message = error.message ?: "Failed to load reviews"
                        )
                    } else {
                        val current = _state.value as? ReviewsState.Content ?: return@fold
                        _state.value = current.copy(isLoadingMore = false)
                    }
                }
            )
        }
    }

    fun loadMore() {
        val current = _state.value as? ReviewsState.Content ?: return
        if (current.isLoadingMore || !current.hasNextPage) return
        loadReviews(current.currentPage + 1)
    }
}
