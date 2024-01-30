package com.grig.myanimelist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grig.myanimelist.data.MalRepository
import com.grig.myanimelist.data.model.MalAnime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val malRepository: MalRepository
) : ViewModel() {

    val animeList = MutableStateFlow(emptyList<MalAnime>())

    init {
        viewModelScope.launch {
            animeList.emit(malRepository.getUserAnimeList())
        }
    }
}