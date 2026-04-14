package com.grig.myanimelist.ui.mangaedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grig.myanimelist.data.MalRepository
import com.grig.myanimelist.data.model.manga.MalMangaReadingStatus
import com.grig.myanimelist.ui.mangalist.MangaCardData
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
class EditMangaViewModel @Inject constructor(
    private val malRepository: MalRepository
) : ViewModel() {

    private val _state = MutableStateFlow(EditMangaState())
    val state: StateFlow<EditMangaState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<EditMangaEvent>()
    val events: SharedFlow<EditMangaEvent> = _events.asSharedFlow()

    fun init(data: MangaCardData) {
        _state.value = EditMangaState.from(data.listStatus)
    }

    fun setStatus(status: MalMangaReadingStatus) {
        _state.update { it.copy(status = status) }
    }

    fun setScore(score: Int) {
        _state.update { it.copy(score = score.coerceIn(0, 10)) }
    }

    fun setChapters(chapters: Int) {
        _state.update { it.copy(numChaptersRead = chapters.coerceAtLeast(0)) }
    }

    fun setVolumes(volumes: Int) {
        _state.update { it.copy(numVolumesRead = volumes.coerceAtLeast(0)) }
    }

    fun setFinishDateToday(totalChapters: Int?, totalVolumes: Int?) {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        _state.update {
            it.copy(
                finishDate = today,
                status = MalMangaReadingStatus.Completed,
                numChaptersRead = totalChapters?.takeIf { ch -> ch > 0 }
                    ?: it.numChaptersRead,
                numVolumesRead = totalVolumes?.takeIf { vol -> vol > 0 }
                    ?: it.numVolumesRead
            )
        }
    }

    fun save(mangaId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, error = null) }
            val s = _state.value
            val result = malRepository.updateMangaListStatus(
                mangaId = mangaId,
                status = s.status.apiValue,
                score = s.score,
                numChaptersRead = s.numChaptersRead,
                numVolumesRead = s.numVolumesRead,
                finishDate = s.finishDate
            )
            result.fold(
                onSuccess = { updatedStatus ->
                    _state.update { it.copy(isSaving = false) }
                    _events.emit(EditMangaEvent.Saved(updatedStatus))
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(isSaving = false, error = error.message ?: "Failed to save")
                    }
                    _events.emit(EditMangaEvent.Error(error.message ?: "Failed to save"))
                }
            )
        }
    }

    fun delete(mangaId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, error = null) }
            val result = malRepository.deleteMangaListItem(mangaId)
            result.fold(
                onSuccess = {
                    _state.update { it.copy(isSaving = false) }
                    _events.emit(EditMangaEvent.Deleted)
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(isSaving = false, error = error.message ?: "Failed to delete")
                    }
                    _events.emit(EditMangaEvent.Error(error.message ?: "Failed to delete"))
                }
            )
        }
    }
}
