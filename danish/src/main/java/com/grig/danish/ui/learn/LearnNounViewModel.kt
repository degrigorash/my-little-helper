package com.grig.danish.ui.learn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grig.danish.data.DanishRepository
import com.grig.danish.ui.sound.WordEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NounForms(
    val singularDefinite: String,
    val plural: String,
    val pluralDefinite: String
)

data class LearnNounUiState(
    val title: String = "Learn Nouns",
    val word: String = "forretningsforbindelserne",
    val translation: String = "The business connections",
    val forms: NounForms = NounForms(
        singularDefinite = "forretningsforbindelsen",
        plural = "forretningsforbindelser",
        pluralDefinite = "forretningsforbindelserne"
    )
)

@HiltViewModel
class LearnNounViewModel @Inject constructor(
    private val repository: DanishRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LearnNounUiState())
    val uiState: StateFlow<LearnNounUiState> = _uiState

    private val _event = MutableSharedFlow<WordEvent>()
    val event = _event.asSharedFlow()

    fun nextWord() {
        // For now, rotate through a simple static example.
        // In real app you'd fetch from repository.
        _uiState.value = LearnNounUiState()
    }

    fun pronounce() {
        val word = _uiState.value.word
        if (word.isBlank()) return
        viewModelScope.launch {
            _event.emit(WordEvent.Loading)
            repository.word(word)
                .onSuccess { link -> _event.emit(WordEvent.Success(link)) }
                .onFailure { _event.emit(WordEvent.Error) }
        }
    }
}
