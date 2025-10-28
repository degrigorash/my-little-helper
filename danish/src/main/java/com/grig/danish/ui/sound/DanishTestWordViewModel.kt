package com.grig.danish.ui.sound

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grig.danish.data.DanishRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DanishTestWordViewModel @Inject constructor(
    private val repository: DanishRepository
) : ViewModel() {

    private val _event = MutableSharedFlow<WordEvent>()
    val event = _event.asSharedFlow()

    fun onMicClick(word: String) {
        if (word.isBlank()) return
        viewModelScope.launch {
            _event.emit(WordEvent.Loading)
            repository.word(word)
                .onSuccess { link ->
                    _event.emit(WordEvent.Success(link))
                }
                .onFailure {
                    _event.emit(WordEvent.Error)
                }
        }
    }
}
