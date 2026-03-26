package com.grig.myanimelist.ui.animedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.grig.myanimelist.MalRoute
import com.grig.myanimelist.data.MalRepository
import com.grig.myanimelist.data.model.MalUserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val malRepository: MalRepository
) : ViewModel() {

    private val animeId: Int = savedStateHandle.toRoute<MalRoute.AnimeDetail>().animeId

    private val _state = MutableStateFlow<AnimeDetailState>(AnimeDetailState.Loading)
    val state: StateFlow<AnimeDetailState> = _state.asStateFlow()

    init {
        loadDetail()
        loadRelatedManga()
    }

    private fun loadDetail() {
        _state.value = AnimeDetailState.Loading
        viewModelScope.launch {
            val result = malRepository.getAnimeDetails(animeId)
            result.fold(
                onSuccess = { anime ->
                    _state.value = AnimeDetailState.Content(anime = anime)
                },
                onFailure = { error ->
                    _state.value = AnimeDetailState.Error(
                        message = error.message ?: "Failed to load details"
                    )
                }
            )
        }
    }

    private fun loadRelatedManga() {
        viewModelScope.launch {
            val relations = malRepository.getAnimeRelatedManga(animeId)
            val current = _state.value
            if (current is AnimeDetailState.Content) {
                _state.value = current.copy(
                    relatedManga = relations,
                    isLoadingRelatedManga = false
                )
            }
        }
    }

    fun addToMyList() {
        val current = _state.value as? AnimeDetailState.Content ?: return
        _state.value = current.copy(isUpdatingList = true)
        viewModelScope.launch {
            val result = malRepository.updateAnimeListStatus(
                animeId = animeId,
                status = "plan_to_watch"
            )
            result.fold(
                onSuccess = { refreshDetail() },
                onFailure = { error ->
                    val state = _state.value as? AnimeDetailState.Content ?: return@fold
                    _state.value = state.copy(isUpdatingList = false)
                }
            )
        }
    }

    fun deleteFromMyList() {
        val current = _state.value as? AnimeDetailState.Content ?: return
        _state.value = current.copy(isUpdatingList = true)
        viewModelScope.launch {
            val result = malRepository.deleteAnimeListItem(animeId)
            result.fold(
                onSuccess = { refreshDetail() },
                onFailure = { error ->
                    val state = _state.value as? AnimeDetailState.Content ?: return@fold
                    _state.value = state.copy(isUpdatingList = false)
                }
            )
        }
    }

    private suspend fun refreshDetail() {
        val result = malRepository.getAnimeDetails(animeId)
        result.fold(
            onSuccess = { anime ->
                val current = _state.value as? AnimeDetailState.Content
                _state.value = AnimeDetailState.Content(
                    anime = anime,
                    isUpdatingList = false,
                    listChanged = true,
                    relatedManga = current?.relatedManga ?: emptyList(),
                    isLoadingRelatedManga = false
                )
            },
            onFailure = {
                val current = _state.value as? AnimeDetailState.Content ?: return
                _state.value = current.copy(isUpdatingList = false)
            }
        )
    }

    suspend fun isAuthorized(): Boolean {
        return malRepository.userFlow.first() is MalUserState.Authorized
    }
}
