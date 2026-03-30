package com.grig.danish.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grig.danish.data.DanishRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DanishHomeViewModel @Inject constructor(
    private val repository: DanishRepository
) : ViewModel() {

    private val _state = MutableStateFlow<DanishHomeState>(DanishHomeState.Loading)
    val state: StateFlow<DanishHomeState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val refreshResult = repository.refreshNouns()
            refreshResult.onFailure { Timber.e(it, "Failed to refresh nouns") }

            repository.getAllNouns().collect { nouns ->
                if (nouns.isEmpty() && refreshResult.isFailure) {
                    _state.value = DanishHomeState.Error(
                        refreshResult.exceptionOrNull()?.message ?: "Failed to load nouns"
                    )
                } else {
                    val folders = nouns.map { it.folder }.distinct().sorted()
                    _state.value = DanishHomeState.Content(
                        nounCount = nouns.size,
                        nounFolders = folders
                    )
                }
            }
        }
    }
}
