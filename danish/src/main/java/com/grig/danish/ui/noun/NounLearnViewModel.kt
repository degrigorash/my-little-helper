package com.grig.danish.ui.noun

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.grig.danish.DanishRoute
import com.grig.danish.data.DanishRepository
import com.grig.danish.data.model.Noun
import com.grig.danish.tools.DanishTextToSpeechPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NounLearnViewModel @Inject constructor(
    private val repository: DanishRepository,
    private val ttsPlayer: DanishTextToSpeechPlayer,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val route = savedStateHandle.toRoute<DanishRoute.NounLearn>()

    private val _state = MutableStateFlow<NounLearnState>(NounLearnState.Loading)
    val state: StateFlow<NounLearnState> = _state.asStateFlow()

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
                _state.value = NounLearnState.Error("No nouns available")
                return@launch
            }
            nouns = if (shuffled) loaded.shuffled() else loaded.sortedBy { it.english.lowercase() }
            currentIndex = 0
            _state.value = NounLearnState.Content(
                noun = nouns.first(),
                mode = mode,
                shuffled = shuffled,
                progress = if (!shuffled) "1 / ${nouns.size}" else ""
            )
        }
    }

    fun reveal() {
        val current = _state.value
        if (current is NounLearnState.Content) {
            _state.value = current.copy(revealed = true)
        }
    }

    fun next() {
        if (nouns.isEmpty()) return
        if (shuffled) {
            _state.value = NounLearnState.Content(
                noun = nouns.random(),
                mode = mode,
                shuffled = true
            )
        } else {
            currentIndex = (currentIndex + 1) % nouns.size
            _state.value = NounLearnState.Content(
                noun = nouns[currentIndex],
                mode = mode,
                shuffled = false,
                progress = "${currentIndex + 1} / ${nouns.size}"
            )
        }
    }

    fun speakDanish() {
        val current = _state.value
        if (current is NounLearnState.Content) {
            ttsPlayer.speak(current.noun.danish)
        }
    }
}
