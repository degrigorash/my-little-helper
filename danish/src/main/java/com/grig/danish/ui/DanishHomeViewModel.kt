package com.grig.danish.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grig.danish.data.DanishRepository
import com.grig.danish.data.TtsSettings
import com.grig.danish.data.TtsSettingsManager
import com.grig.danish.tools.DanishTextToSpeechPlayer
import com.grig.danish.tools.TtsVoiceInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DanishHomeViewModel @Inject constructor(
    private val repository: DanishRepository,
    private val ttsSettingsManager: TtsSettingsManager,
    private val ttsPlayer: DanishTextToSpeechPlayer
) : ViewModel() {

    private val _state = MutableStateFlow<DanishHomeState>(DanishHomeState.Loading)
    val state: StateFlow<DanishHomeState> = _state.asStateFlow()

    val ttsSettings: StateFlow<TtsSettings> = ttsSettingsManager.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TtsSettings())

    val availableVoices: StateFlow<List<TtsVoiceInfo>> = ttsPlayer.availableVoices

    private val _showTtsSettings = MutableStateFlow(false)
    val showTtsSettings: StateFlow<Boolean> = _showTtsSettings.asStateFlow()

    init {
        viewModelScope.launch {
            val refreshResult = repository.refreshNouns()
            refreshResult.onFailure { Timber.e(it, "Failed to refresh nouns") }

            repository.getAllNouns().collect { nouns ->
                if (nouns.isEmpty() && refreshResult.isFailure) {
                    _state.value = DanishHomeState.Error(
                        refreshResult.exceptionOrNull()?.message ?: "Failed to load nouns"
                    )
                } else {
                    val folders = nouns.map { it.folder }.distinct().sorted()
                    _state.value = DanishHomeState.Content(
                        nounCount = nouns.size,
                        nounFolders = folders
                    )
                }
            }
        }

        viewModelScope.launch {
            ttsSettingsManager.settings.collect { settings ->
                ttsPlayer.setSpeechRate(settings.speechRate)
                ttsPlayer.setPitch(settings.pitch)
                ttsPlayer.setVoice(settings.voiceName)
            }
        }
    }

    fun openTtsSettings() {
        _showTtsSettings.value = true
    }

    fun closeTtsSettings() {
        _showTtsSettings.value = false
    }

    fun updateTtsSettings(settings: TtsSettings) {
        viewModelScope.launch {
            ttsSettingsManager.updateSettings(settings)
        }
    }
}
