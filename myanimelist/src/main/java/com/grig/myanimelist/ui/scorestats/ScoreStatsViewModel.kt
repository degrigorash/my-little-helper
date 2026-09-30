package com.grig.myanimelist.ui.scorestats

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.grig.myanimelist.MalRoute
import com.grig.myanimelist.data.MalRepository
import com.grig.myanimelist.data.model.anime.MalAnimeWatchingStatus
import com.grig.myanimelist.data.model.jikan.JikanScoreBucket
import com.grig.myanimelist.data.model.jikan.JikanStatistics
import com.grig.myanimelist.data.model.manga.MalMangaReadingStatus
import com.grig.myanimelist.data.toJikanErrorMessage
import com.grig.myanimelist.ui.home.readingStatusColor
import com.grig.myanimelist.ui.home.watchingStatusColor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScoreStatsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val malRepository: MalRepository
) : ViewModel() {

    private val route = savedStateHandle.toRoute<MalRoute.ScoreStats>()
    private val mediaType = ScoreStatsMediaType.fromValue(route.mediaType)
    val mean: Float = route.mean

    private val _state = MutableStateFlow<ScoreStatsState>(ScoreStatsState.Loading)
    val state: StateFlow<ScoreStatsState> = _state.asStateFlow()

    init {
        loadStatistics()
    }

    fun retry() {
        loadStatistics()
    }

    private fun loadStatistics() {
        _state.value = ScoreStatsState.Loading
        viewModelScope.launch {
            val result = when (mediaType) {
                ScoreStatsMediaType.ANIME -> malRepository.getAnimeStatistics(route.mediaId)
                ScoreStatsMediaType.MANGA -> malRepository.getMangaStatistics(route.mediaId)
            }
            result.fold(
                onSuccess = { response -> _state.value = response.data.toState() },
                onFailure = { error ->
                    _state.value = ScoreStatsState.Error(
                        message = error.toJikanErrorMessage("Failed to load statistics")
                    )
                }
            )
        }
    }

    private fun JikanStatistics.toState(): ScoreStatsState {
        // Buckets with no votes may be omitted by the API; fill them so the chart
        // always shows the full 10 → 1 scale.
        val byScore = scores.associateBy { it.score }
        val buckets = (10 downTo 1).map { score -> byScore[score] ?: JikanScoreBucket(score) }
        val totalVotes = buckets.sumOf { it.votes }
        if (totalVotes == 0 && total == 0) return ScoreStatsState.Empty

        return ScoreStatsState.Content(
            scores = buckets,
            totalVotes = totalVotes,
            statuses = statusSlices(),
            totalMembers = total
        )
    }

    // Ordered like MAL's stats page: in progress, completed, on hold, dropped, planned.
    private fun JikanStatistics.statusSlices(): List<StatusSlice> = when (mediaType) {
        ScoreStatsMediaType.ANIME -> listOf(
            MalAnimeWatchingStatus.Watching to (watching ?: 0),
            MalAnimeWatchingStatus.Completed to completed,
            MalAnimeWatchingStatus.OnHold to onHold,
            MalAnimeWatchingStatus.Dropped to dropped,
            MalAnimeWatchingStatus.PlanToWatch to (planToWatch ?: 0)
        ).map { (status, count) ->
            StatusSlice(status.displayName, count, watchingStatusColor(status))
        }
        ScoreStatsMediaType.MANGA -> listOf(
            MalMangaReadingStatus.Reading to (reading ?: 0),
            MalMangaReadingStatus.Completed to completed,
            MalMangaReadingStatus.OnHold to onHold,
            MalMangaReadingStatus.Dropped to dropped,
            MalMangaReadingStatus.PlanToRead to (planToRead ?: 0)
        ).map { (status, count) ->
            StatusSlice(status.displayName, count, readingStatusColor(status))
        }
    }
}
