package com.grig.danish.ui.quiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grig.danish.data.DanishRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VocabularyQuizViewModel @Inject constructor(
    private val repository: DanishRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow<QuizState>(QuizState.Loading)
    val state: StateFlow<QuizState> = _state

//    private val _event = MutableSharedFlow<WordEvent>()
//    val event = _event.asSharedFlow()

    private val words = listOf(
        "hund", "æble", "rød"
    )

    init {
        pickWord()
    }

    fun updateAnswer(value: String) {
//        _state.value = _state.value.copy(userAnswer = value, result = AnswerResult.NONE)
    }

    fun pronounce() {
//        val word = _uiState.value.word
//        if (word.isBlank()) return
//        viewModelScope.launch {
//            _event.emit(WordEvent.Loading)
//            repository.word(word)
//                .onSuccess { link -> _event.emit(WordEvent.Success(link)) }
//                .onFailure { _event.emit(WordEvent.Error) }
//        }
    }

    fun checkAnswer() {
//        val state = _uiState.value
//        val correct = state.userAnswer.trim().equals(state.correctAnswer, ignoreCase = true)
//        _uiState.value = state.copy(result = if (correct) AnswerResult.CORRECT else AnswerResult.INCORRECT)
    }

    fun next() {
//        val state = _uiState.value
//        _uiState.value = state.copy(
//            step = (state.step % state.total) + 1,
//            userAnswer = "",
//            result = AnswerResult.NONE
//        )
    }

    private fun pickWord() {
        viewModelScope.launch {
            words.random().let {
                _state.value = QuizState.Word(it)
            }
        }
    }
}
