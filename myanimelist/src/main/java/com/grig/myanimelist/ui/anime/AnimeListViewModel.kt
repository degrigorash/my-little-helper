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
    val animeList = MutableStateFlow(emptyList<MalAnime>())

    init {

        viewModelScope.launch {
            animeList.emit(malRepository.getUserAnimeList())
        }
    }
}