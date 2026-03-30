package com.grig.danish.data

data class TtsSettings(
    val speechRate: Float = DEFAULT_RATE,
    val pitch: Float = DEFAULT_PITCH,
    val voiceName: String? = null
) {
    companion object {
        const val DEFAULT_RATE = 1.0f
        const val DEFAULT_PITCH = 1.0f
        const val MIN_RATE = 0.25f
        const val MAX_RATE = 2.0f
        const val MIN_PITCH = 0.5f
        const val MAX_PITCH = 2.0f
    }
}
