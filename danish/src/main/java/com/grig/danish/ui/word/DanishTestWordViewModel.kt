package com.grig.danish.ui.word

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grig.danish.data.DanishRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DanishTestWordViewModel @Inject constructor(
    private val repository: DanishRepository
) : ViewModel() {

    fun onMicClick(word: String) {
        if (word.isBlank()) return
        viewModelScope.launch {
            repository.word(word)
                .onSuccess {
                    Timber.wtf(it)
                }
                .onFailure {
                    Timber.wtf(it)
                }
        }
    }
}
