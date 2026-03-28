package com.grig.myanimelist.ui.studiodetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.grig.myanimelist.MalRoute
import com.grig.myanimelist.data.MalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudioDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val malRepository: MalRepository
) : ViewModel() {

    private val studioId: Int = savedStateHandle.toRoute<MalRoute.StudioDetail>().studioId

    private val _state = MutableStateFlow<StudioDetailState>(StudioDetailState.Loading)
    val state: StateFlow<StudioDetailState> = _state.asStateFlow()

    private var currentPage = 1

    init {
        loadProducer()
    }

    private fun loadProducer() {
        _state.value = StudioDetailState.Loading
        viewModelScope.launch {
            malRepository.getProducer(studioId).fold(
                onSuccess = { response ->
                    _state.value = StudioDetailState.Content(producer = response.data)
                    loadAnime()
                },
                onFailure = { error ->
                    _state.value = StudioDetailState.Error(
                        message = error.message ?: "Failed to load studio"
                    )
                }
            )
        }
    }

    private fun loadAnime() {
        viewModelScope.launch {
            malRepository.getAnimeByProducer(studioId, page = currentPage).fold(
                onSuccess = { response ->
                    val current = _state.value as? StudioDetailState.Content ?: return@fold
                    _state.value = current.copy(
                        animeList = (current.animeList + response.data).distinctBy { it.malId },
                        isLoadingAnime = false,
                        hasMoreAnime = response.pagination.hasNextPage,
                        isLoadingMore = false
                    )
                },
                onFailure = {
                    val current = _state.value as? StudioDetailState.Content ?: return@fold
                    _state.value = current.copy(
                        isLoadingAnime = false,
                        isLoadingMore = false
                    )
                }
            )
        }
    }

    fun loadMore() {
        val current = _state.value as? StudioDetailState.Content ?: return
        if (current.isLoadingMore || !current.hasMoreAnime) return
        _state.value = current.copy(isLoadingMore = true)
        currentPage++
        loadAnime()
    }
}
