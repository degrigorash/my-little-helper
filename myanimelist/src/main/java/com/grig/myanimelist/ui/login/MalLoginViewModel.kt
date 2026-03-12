package com.grig.myanimelist.ui.login

import androidx.lifecycle.ViewModel
import com.grig.myanimelist.data.MalRepository
import com.grig.myanimelist.data.model.MalUserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MalLoginViewModel @Inject constructor(
    private val malRepository: MalRepository
) : ViewModel() {

    val userFlow: Flow<MalUserState> = malRepository.userFlow

    fun loginUri(): String = malRepository.loginUri()
}
