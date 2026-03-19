package com.grig.myanimelist.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grig.myanimelist.data.MalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MalHomeViewModel @Inject constructor(
    private val malRepository: MalRepository
) : ViewModel() {

    val malUserFlow = malRepository.userFlow

    private val _activeTab = MutableStateFlow(MalTab.Anime)
    val activeTab: StateFlow<MalTab> = _activeTab.asStateFlow()

    private val _guestUsername = MutableStateFlow("")
    val guestUsername: StateFlow<String> = _guestUsername.asStateFlow()

    fun malLogin(openUri: (String) -> Unit) {
        openUri(malRepository.loginUri())
    }

    fun malLogout() {
        viewModelScope.launch {
            malRepository.logout()
        }
    }

    fun selectTab(tab: MalTab) {
        if (_activeTab.value == tab) return
        _activeTab.value = tab
    }

    fun setGuestUsername(name: String) {
        _guestUsername.value = name
    }
}
