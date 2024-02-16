package com.grig.myanimelist.ui.manga

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grig.myanimelist.data.MalRepository
import com.grig.myanimelist.data.model.manga.MalManga
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MangaListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val malRepository: MalRepository
) : ViewModel() {

    private val username: String? = savedStateHandle["username"]
    private var offset = 0
    private var mangas = mutableListOf<MalManga>()
    val state: MutableStateFlow<MangaUiState> = MutableStateFlow(MangaUiState.Loading)

    init {
        reloadList()
    }

    private fun reloadList() {
        offset = 0
        mangas = mutableListOf()
        viewModelScope.launch {
            var response = malRepository.getUserMangaList(
                username = username ?: "@me",
                offset = offset
            )
            if (response.isFailure) {
                state.emit(MangaUiState.Error(response.exceptionOrNull()))
            }

            while (response.isSuccess) {
                val body = response.getOrNull()
                if (body?.data == null) break
                mangas.addAll(body.data.map { it.manga })
                offset += body.data.size
                response = malRepository.getUserMangaList(
                    username = username ?: "@me",
                    offset = offset
                )
                if (body.data.size < 100) break
            }
            when {
                mangas.size > 0 -> state.emit(MangaUiState.Content(mangas.sortedByDescending { it.mean }))
                else -> state.emit(MangaUiState.Empty)
            }
        }
    }
}