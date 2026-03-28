package com.grig.myanimelist.ui.authordetail

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
class AuthorDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val malRepository: MalRepository
) : ViewModel() {

    private val authorId: Int = savedStateHandle.toRoute<MalRoute.AuthorDetail>().authorId

    private val _state = MutableStateFlow<AuthorDetailState>(AuthorDetailState.Loading)
    val state: StateFlow<AuthorDetailState> = _state.asStateFlow()

    init {
        loadPerson()
    }

    private fun loadPerson() {
        _state.value = AuthorDetailState.Loading
        viewModelScope.launch {
            malRepository.getPersonFull(authorId).fold(
                onSuccess = { response ->
                    _state.value = AuthorDetailState.Content(person = response.data)
                },
                onFailure = { error ->
                    _state.value = AuthorDetailState.Error(
                        message = error.message ?: "Failed to load author"
                    )
                }
            )
        }
    }
}
