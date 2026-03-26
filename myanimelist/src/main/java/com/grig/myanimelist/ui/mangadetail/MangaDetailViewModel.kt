package com.grig.myanimelist.ui.mangadetail

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
class MangaDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val malRepository: MalRepository
) : ViewModel() {

    private val mangaId: Int = savedStateHandle.toRoute<MalRoute.MangaDetail>().mangaId

    private val _state = MutableStateFlow<MangaDetailState>(MangaDetailState.Loading)
    val state: StateFlow<MangaDetailState> = _state.asStateFlow()

    init {
        loadDetail()
        loadRelatedAnime()
    }

    private fun loadDetail() {
        _state.value = MangaDetailState.Loading
        viewModelScope.launch {
            val result = malRepository.getMangaDetails(mangaId)
            result.fold(
                onSuccess = { manga ->
                    _state.value = MangaDetailState.Content(manga = manga)
                },
                onFailure = { error ->
                    _state.value = MangaDetailState.Error(
                        message = error.message ?: "Failed to load details"
                    )
                }
            )
        }
    }

    private fun loadRelatedAnime() {
        viewModelScope.launch {
            val relations = malRepository.getMangaRelatedAnime(mangaId)
            val current = _state.value
            if (current is MangaDetailState.Content) {
                _state.value = current.copy(
                    relatedAnime = relations,
                    isLoadingRelatedAnime = false
                )
            }
        }
    }

    fun addToMyList() {
        val current = _state.value as? MangaDetailState.Content ?: return
        _state.value = current.copy(isUpdatingList = true)
        viewModelScope.launch {
            val result = malRepository.updateMangaListStatus(
                mangaId = mangaId,
                status = "plan_to_read"
            )
            result.fold(
                onSuccess = { refreshDetail() },
                onFailure = { error ->
                    val state = _state.value as? MangaDetailState.Content ?: return@fold
                    _state.value = state.copy(isUpdatingList = false)
                }
            )
        }
    }

    fun deleteFromMyList() {
        val current = _state.value as? MangaDetailState.Content ?: return
        _state.value = current.copy(isUpdatingList = true)
        viewModelScope.launch {
            val result = malRepository.deleteMangaListItem(mangaId)
            result.fold(
                onSuccess = { refreshDetail() },
                onFailure = { error ->
                    val state = _state.value as? MangaDetailState.Content ?: return@fold
                    _state.value = state.copy(isUpdatingList = false)
                }
            )
        }
    }

    private suspend fun refreshDetail() {
        val result = malRepository.getMangaDetails(mangaId)
        result.fold(
            onSuccess = { manga ->
                val current = _state.value as? MangaDetailState.Content
                _state.value = MangaDetailState.Content(
                    manga = manga,
                    isUpdatingList = false,
                    listChanged = true,
                    relatedAnime = current?.relatedAnime ?: emptyList(),
                    isLoadingRelatedAnime = false
                )
            },
            onFailure = {
                val current = _state.value as? MangaDetailState.Content ?: return
                _state.value = current.copy(isUpdatingList = false)
            }
        )
    }

    suspend fun isAuthorized(): Boolean {
        return malRepository.userFlow.first() is MalUserState.Authorized
    }
}
