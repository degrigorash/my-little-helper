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

    fun tryLogin() {
        viewModelScope.launch {
            malRepository.tryLogin()
        }
    }
}