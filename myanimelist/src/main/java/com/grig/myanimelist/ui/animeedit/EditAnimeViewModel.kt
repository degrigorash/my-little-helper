package com.grig.myanimelist.ui.animeedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grig.myanimelist.data.MalRepository
import com.grig.myanimelist.data.local.WatchlistDao
import com.grig.myanimelist.data.local.WatchlistEntry
import com.grig.myanimelist.data.model.anime.MalAnimeWatchingStatus
import com.grig.myanimelist.ui.animelist.AnimeCardData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class EditAnimeViewModel @Inject constructor(
    private val malRepository: MalRepository,
    private val watchlistDao: WatchlistDao
) : ViewModel() {

    private val _state = MutableStateFlow(EditAnimeState())
    val state: StateFlow<EditAnimeState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<EditAnimeEvent>()
    val events: SharedFlow<EditAnimeEvent> = _events.asSharedFlow()

    fun init(data: AnimeCardData) {
        _state.value = EditAnimeState.from(data.listStatus)
        viewModelScope.launch {
            val entry = watchlistDao.getByAnimeId(data.anime.id)
            _state.update { it.copy(isBookmarked = entry != null) }
        }
    }

    fun setStatus(status: MalAnimeWatchingStatus) {
        _state.update { it.copy(status = status) }
    }

    fun setScore(score: Int) {
        _state.update { it.copy(score = score.coerceIn(0, 10)) }
    }

    fun setEpisodes(episodes: Int) {
        _state.update { it.copy(numEpisodesWatched = episodes.coerceAtLeast(0)) }
    }

    fun setFinishDateToday(totalEpisodes: Int?) {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        _state.update {
            it.copy(
                finishDate = today,
                status = MalAnimeWatchingStatus.Completed,
                numEpisodesWatched = totalEpisodes?.takeIf { ep -> ep > 0 }
                    ?: it.numEpisodesWatched
            )
        }
    }

    fun toggleBookmarkEditor() {
        _state.update { it.copy(showBookmarkEditor = !it.showBookmarkEditor) }
    }

    fun setBookmarkNotes(notes: String) {
        _state.update { it.copy(bookmarkNotes = notes) }
    }

    fun saveBookmark(animeId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, error = null) }
            val notes = _state.value.bookmarkNotes
            watchlistDao.insert(WatchlistEntry(animeId = animeId))
            val result = malRepository.updateAnimeListStatus(
                animeId = animeId,
                comments = notes.ifBlank { null }
            )
            result.fold(
                onSuccess = {
                    _state.update {
                        it.copy(
                            isSaving = false,
                            isBookmarked = true,
                            showBookmarkEditor = false
                        )
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            isSaving = false,
                            isBookmarked = true,
                            showBookmarkEditor = false
                        )
                    }
                }
            )
        }
    }

    fun removeBookmark(animeId: Int) {
        viewModelScope.launch {
            watchlistDao.delete(animeId)
            _state.update {
                it.copy(
                    isBookmarked = false,
                    showBookmarkEditor = false
                )
            }
        }
    }

    fun save(animeId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, error = null) }
            val s = _state.value
            val result = malRepository.updateAnimeListStatus(
                animeId = animeId,
                status = s.status.apiValue,
                score = s.score,
                numWatchedEpisodes = s.numEpisodesWatched,
                finishDate = s.finishDate
            )
            result.fold(
                onSuccess = { updatedStatus ->
                    _state.update { it.copy(isSaving = false) }
                    _events.emit(EditAnimeEvent.Saved(updatedStatus))
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(isSaving = false, error = error.message ?: "Failed to save")
                    }
                    _events.emit(EditAnimeEvent.Error(error.message ?: "Failed to save"))
                }
            )
        }
    }

    fun delete(animeId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, error = null) }
            val result = malRepository.deleteAnimeListItem(animeId)
            result.fold(
                onSuccess = {
                    _state.update { it.copy(isSaving = false) }
                    _events.emit(EditAnimeEvent.Deleted)
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(isSaving = false, error = error.message ?: "Failed to delete")
                    }
                    _events.emit(EditAnimeEvent.Error(error.message ?: "Failed to delete"))
                }
            )
        }
    }
}
