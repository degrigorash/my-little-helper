package com.grig.danish.tools

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import java.util.Locale

data class TtsVoiceInfo(
    val name: String,
    val label: String
)

class DanishTextToSpeechPlayer(context: Context) {

    private var tts: TextToSpeech? = null
    private var ready = false
    private var pendingRate: Float? = null
    private var pendingPitch: Float? = null
    private var pendingVoiceName: String? = null
    private var danishVoices: Map<String, Voice> = emptyMap()

    private val _availableVoices = MutableStateFlow<List<TtsVoiceInfo>>(emptyList())
    val availableVoices: StateFlow<List<TtsVoiceInfo>> = _availableVoices.asStateFlow()

    init {
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale("da", "DK"))
                ready = result != TextToSpeech.LANG_MISSING_DATA
                        && result != TextToSpeech.LANG_NOT_SUPPORTED
                if (!ready) {
                    Timber.w("Danish TTS not available on this device")
                } else {
                    loadDanishVoices()
                    pendingRate?.let { tts?.setSpeechRate(it) }
                    pendingPitch?.let { tts?.setPitch(it) }
                    pendingVoiceName?.let { applyVoice(it) }
                }
            } else {
                Timber.e("TTS initialization failed with status: $status")
            }
        }
    }

    private fun loadDanishVoices() {
        val voices = tts?.voices?.filter { it.locale.language == "da" } ?: return
        danishVoices = voices.associateBy { it.name }
        _availableVoices.value = voices
            .sortedWith(compareBy({ it.isNetworkConnectionRequired }, { it.name }))
            .map { voice ->
                TtsVoiceInfo(
                    name = voice.name,
                    label = buildVoiceLabel(voice)
                )
            }
    }

    private fun buildVoiceLabel(voice: Voice): String {
        val parts = mutableListOf<String>()
        val quality = when (voice.quality) {
            Voice.QUALITY_VERY_HIGH -> "Very High"
            Voice.QUALITY_HIGH -> "High"
            Voice.QUALITY_NORMAL -> "Normal"
            Voice.QUALITY_LOW -> "Low"
            Voice.QUALITY_VERY_LOW -> "Very Low"
            else -> null
        }
        if (quality != null) parts += quality
        if (voice.isNetworkConnectionRequired) parts += "Online"
        val suffix = if (parts.isNotEmpty()) " (${parts.joinToString(", ")})" else ""
        return voice.name + suffix
    }

    fun speak(text: String) {
        if (ready) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, text.hashCode().toString())
        } else {
            Timber.w("TTS not ready, cannot speak: $text")
        }
    }

    fun setSpeechRate(rate: Float) {
        pendingRate = rate
        if (ready) tts?.setSpeechRate(rate)
    }

    fun setPitch(pitch: Float) {
        pendingPitch = pitch
        if (ready) tts?.setPitch(pitch)
    }

    fun setVoice(voiceName: String?) {
        pendingVoiceName = voiceName
        if (ready && voiceName != null) applyVoice(voiceName)
    }

    private fun applyVoice(voiceName: String) {
        val voice = danishVoices[voiceName]
        if (voice != null) {
            tts?.voice = voice
        } else {
            Timber.w("Voice not found: $voiceName")
        }
    }

    fun shutdown() {
        tts?.shutdown()
        tts = null
        ready = false
    }
}
