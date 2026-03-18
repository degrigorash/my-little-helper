package com.grig.myanimelist.ui.mangadetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.grig.myanimelist.MalRoute
import com.grig.myanimelist.data.MalRepository
import com.grig.myanimelist.data.model.MalUserState
import com.grig.myanimelist.data.model.manga.MalManga
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MangaDetailState(
    val manga: MalManga? = null,
    val isLoading: Boolean = true,
    val isUpdatingList: Boolean = false,
    val listChanged: Boolean = false,
    val error: String? = null
) {
    val isInMyList: Boolean
        get() = manga?.myListStatus != null
}

@HiltViewModel
class MangaDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val malRepository: MalRepository
) : ViewModel() {

    private val mangaId: Int = savedStateHandle.toRoute<MalRoute.MangaDetail>().mangaId

    private val _state = MutableStateFlow(MangaDetailState())
    val state: StateFlow<MangaDetailState> = _state.asStateFlow()

    init {
        loadDetail()
    }

    private fun loadDetail() {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val result = malRepository.getMangaDetails(mangaId)
            result.fold(
                onSuccess = { manga ->
                    _state.update { it.copy(manga = manga, isLoading = false) }
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
            val result = malRepository.updateMangaListStatus(
                mangaId = mangaId,
                status = "plan_to_read"
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
            val result = malRepository.deleteMangaListItem(mangaId)
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
        val result = malRepository.getMangaDetails(mangaId)
        result.fold(
            onSuccess = { manga ->
                _state.update {
                    it.copy(manga = manga, isUpdatingList = false, listChanged = true)
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
