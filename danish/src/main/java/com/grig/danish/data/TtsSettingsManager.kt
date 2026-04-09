package com.grig.danish.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

class TtsSettingsManager @Inject constructor(
    @param:Named("DanishDataStore")
    private val dataStore: DataStore<Preferences>
) {

    val settings: Flow<TtsSettings> = dataStore.data.map { prefs ->
        TtsSettings(
            speechRate = prefs[SPEECH_RATE] ?: TtsSettings.DEFAULT_RATE,
            pitch = prefs[PITCH] ?: TtsSettings.DEFAULT_PITCH,
            voiceName = prefs[VOICE_NAME]
        )
    }

    suspend fun updateSettings(settings: TtsSettings) {
        dataStore.edit { prefs ->
            prefs[SPEECH_RATE] = settings.speechRate
            prefs[PITCH] = settings.pitch
            if (settings.voiceName != null) {
                prefs[VOICE_NAME] = settings.voiceName
            } else {
                prefs.remove(VOICE_NAME)
            }
        }
    }

    companion object {
        private val SPEECH_RATE = floatPreferencesKey("tts_speech_rate")
        private val PITCH = floatPreferencesKey("tts_pitch")
        private val VOICE_NAME = stringPreferencesKey("tts_voice_name")
    }
}
