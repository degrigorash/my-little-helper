package com.grig.myanimelist.ui.manga

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grig.myanimelist.data.MalRepository
import com.grig.myanimelist.data.model.anime.MalAnime
import com.grig.myanimelist.data.model.manga.MalManga
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MangaListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val malRepository: MalRepository
) : ViewModel() {

    private val username: String? = savedStateHandle["username"]
    val mangaList = MutableStateFlow(emptyList<MalManga>())

    init {
        viewModelScope.launch {
            mangaList.emit(malRepository.getUserMangaList())
        }
    }
}