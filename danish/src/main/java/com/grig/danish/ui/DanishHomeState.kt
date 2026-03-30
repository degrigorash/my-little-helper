package com.grig.danish.ui

sealed interface DanishHomeState {

    data object Loading : DanishHomeState

    data class Content(
        val nounCount: Int,
        val nounFolders: List<String>
    ) : DanishHomeState

    data class Error(val message: String) : DanishHomeState
}
