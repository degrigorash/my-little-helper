package com.grig.mylittlehelper.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grig.myanimelist.data.MalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val malRepository: MalRepository
) : ViewModel() {

    val malUserFlow = malRepository.userFlow

    fun malLogin(openUri: (String) -> Unit) {
        openUri(malRepository.loginUri())
    }

    fun malLogout() {
        viewModelScope.launch {
            malRepository.logout()
        }
    }
}