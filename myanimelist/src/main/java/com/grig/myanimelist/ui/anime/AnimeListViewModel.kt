package com.grig.myanimelist.ui.anime

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grig.myanimelist.data.MalRepository
import com.grig.myanimelist.data.model.anime.MalAnime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val malRepository: MalRepository
) : ViewModel() {

    private val username: String? = savedStateHandle["username"]
    private var offset = 0
    private var animes = mutableListOf<MalAnime>()
    val state: MutableStateFlow<AnimeUiState> = MutableStateFlow(AnimeUiState.Loading)

    init {
        reloadList()
    }

    private fun reloadList() {
        offset = 0
        animes = mutableListOf()
        viewModelScope.launch {
            var response = malRepository.getUserAnimeList(
                username = username ?: "@me",
                offset = offset
            )
            if (response.isFailure) {
                state.emit(AnimeUiState.Error(response.exceptionOrNull()))
            }

            while (response.isSuccess) {
                val body = response.getOrNull()
                if (body?.data == null) break
                animes.addAll(body.data.map { it.anime })
                offset += body.data.size
                response = malRepository.getUserAnimeList(
                    username = username ?: "@me",
                    offset = offset
                )
                if (body.data.size < 100) break
            }
            when {
                animes.size > 0 -> state.emit(AnimeUiState.Content(animes.sortedByDescending { it.mean }))
                else -> state.emit(AnimeUiState.Empty)
            }
        }
    }
}