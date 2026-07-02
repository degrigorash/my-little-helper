package com.grig.myanimelist.ui.persondetail

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
class PersonDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val malRepository: MalRepository
) : ViewModel() {

    private val route = savedStateHandle.toRoute<MalRoute.PersonDetail>()
    private val personId: Int = route.personId

    private val _state = MutableStateFlow<PersonDetailState>(PersonDetailState.Loading(progress = 0f))
    val state: StateFlow<PersonDetailState> = _state.asStateFlow()

    init {
        loadPerson()
    }

    private fun loadPerson() {
        _state.value = PersonDetailState.Loading(progress = 0f)
        viewModelScope.launch {
            malRepository.getPersonWithVoices(
                personId = personId,
                onFavoritesProgress = { done, total ->
                    if (total > 0) {
                        _state.value = PersonDetailState.Loading(progress = done.toFloat() / total)
                    }
                }
            ).fold(
                onSuccess = { person ->
                    _state.value = PersonDetailState.Content(person = person)
                },
                onFailure = { error ->
                    _state.value = PersonDetailState.Error(
                        message = error.message ?: "Failed to load person"
                    )
                }
            )
        }
    }
}
