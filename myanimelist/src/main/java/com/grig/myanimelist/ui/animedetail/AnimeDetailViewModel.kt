package com.grig.myanimelist.ui.animedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.grig.myanimelist.MalRoute
import com.grig.myanimelist.data.MalRepository
import com.grig.myanimelist.data.model.MalUserState
import com.grig.myanimelist.data.model.anime.MalAnime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AnimeDetailState(
    val anime: MalAnime? = null,
    val isLoading: Boolean = true,
    val isUpdatingList: Boolean = false,
    val listChanged: Boolean = false,
    val error: String? = null
) {
    val isInMyList: Boolean
        get() = anime?.myListStatus != null
}

@HiltViewModel
class AnimeDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val malRepository: MalRepository
) : ViewModel() {

    private val animeId: Int = savedStateHandle.toRoute<MalRoute.AnimeDetail>().animeId

    private val _state = MutableStateFlow(AnimeDetailState())
    val state: StateFlow<AnimeDetailState> = _state.asStateFlow()

    init {
        loadDetail()
    }

    private fun loadDetail() {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val result = malRepository.getAnimeDetails(animeId)
            result.fold(
                onSuccess = { anime ->
                    _state.update { it.copy(anime = anime, isLoading = false) }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(isLoading = false, error = error.message ?: "Failed to load details")
                    }
                }
            )
        }
    }

    fun addToMyList() {
        _state.update { it.copy(isUpdatingList = true, error = null) }
        viewModelScope.launch {
            val result = malRepository.updateAnimeListStatus(
                animeId = animeId,
                status = "plan_to_watch"
            )
            result.fold(
                onSuccess = { refreshDetail() },
                onFailure = { error ->
                    _state.update {
                        it.copy(isUpdatingList = false, error = error.message ?: "Failed to add")
                    }
                }
            )
        }
    }

    fun deleteFromMyList() {
        _state.update { it.copy(isUpdatingList = true, error = null) }
        viewModelScope.launch {
            val result = malRepository.deleteAnimeListItem(animeId)
            result.fold(
                onSuccess = { refreshDetail() },
                onFailure = { error ->
                    _state.update {
                        it.copy(isUpdatingList = false, error = error.message ?: "Failed to delete")
                    }
                }
            )
        }
    }

    private suspend fun refreshDetail() {
        val result = malRepository.getAnimeDetails(animeId)
        result.fold(
            onSuccess = { anime ->
                _state.update {
                    it.copy(anime = anime, isUpdatingList = false, listChanged = true)
                }
            },
            onFailure = {
                _state.update { it.copy(isUpdatingList = false) }
            }
        )
    }

    suspend fun isAuthorized(): Boolean {
        return malRepository.userFlow.first() is MalUserState.Authorized
    }
}
