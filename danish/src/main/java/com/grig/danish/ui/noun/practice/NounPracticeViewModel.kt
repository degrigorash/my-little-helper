package com.grig.danish.ui.noun.practice

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.grig.danish.DanishRoute
import com.grig.danish.data.DanishRepository
import com.grig.danish.data.model.Noun
import com.grig.danish.tools.DanishTextToSpeechPlayer
import com.grig.danish.ui.LearnMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NounPracticeViewModel @Inject constructor(
    private val repository: DanishRepository,
    private val ttsPlayer: DanishTextToSpeechPlayer,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val route = savedStateHandle.toRoute<DanishRoute.NounPractice>()

    private val _state = MutableStateFlow<NounPracticeState>(NounPracticeState.Loading)
    val state: StateFlow<NounPracticeState> = _state.asStateFlow()

    private val mode = route.mode
    private val shuffled = route.shuffled

    private var nouns: List<Noun> = emptyList()
    private var currentIndex = 0

    init {
        loadNouns()
    }

    private fun loadNouns() {
        viewModelScope.launch {
            val flow = if (route.folder != null) {
                repository.getNounsByFolder(route.folder)
            } else {
                repository.getAllNouns()
            }
            val loaded = flow.first()
            if (loaded.isEmpty()) {
                _state.value = NounPracticeState.Error("No nouns available")
                return@launch
            }
            nouns = if (shuffled) loaded.shuffled() else loaded.sortedBy { it.english.lowercase() }
            currentIndex = 0
            _state.value = NounPracticeState.Content(
                noun = nouns.first(),
                mode = mode,
                shuffled = shuffled,
                progress = if (!shuffled) "1 / ${nouns.size}" else ""
            )
        }
    }

    fun onAnswerChanged(text: String) {
        val current = _state.value
        if (current is NounPracticeState.Content && current.answerResult == AnswerResult.UNANSWERED) {
            _state.value = current.copy(userAnswer = text)
        }
    }

    fun checkAnswer() {
        val current = _state.value
        if (current !is NounPracticeState.Content) return
        if (current.answerResult != AnswerResult.UNANSWERED) return

        val input = current.userAnswer.trim()
        val isCorrect = when (mode) {
            LearnMode.EN_TO_DK -> input.equals(current.noun.danish, ignoreCase = true)
            LearnMode.DK_TO_EN -> {
                val accepted = listOf(current.noun.english) + current.noun.alt
                accepted.any { it.equals(input, ignoreCase = true) }
            }
        }

        _state.value = current.copy(
            answerResult = if (isCorrect) AnswerResult.CORRECT else AnswerResult.INCORRECT
        )
    }

    fun showAnswer() {
        val current = _state.value
        if (current is NounPracticeState.Content && current.answerResult == AnswerResult.UNANSWERED) {
            _state.value = current.copy(answerResult = AnswerResult.INCORRECT)
        }
    }

    fun next() {
        if (nouns.isEmpty()) return
        if (shuffled) {
            _state.value = NounPracticeState.Content(
                noun = nouns.random(),
                mode = mode,
                shuffled = true
            )
        } else {
            currentIndex = (currentIndex + 1) % nouns.size
            _state.value = NounPracticeState.Content(
                noun = nouns[currentIndex],
                mode = mode,
                shuffled = false,
                progress = "${currentIndex + 1} / ${nouns.size}"
            )
        }
    }

    fun speak(text: String) {
        ttsPlayer.speak(text)
    }
}
