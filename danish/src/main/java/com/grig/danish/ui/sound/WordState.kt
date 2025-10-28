package com.grig.danish.ui.sound

sealed class WordEvent {
    data object Loading : WordEvent()
    data class Success(val link: String) : WordEvent()
    data object Error : WordEvent()
}