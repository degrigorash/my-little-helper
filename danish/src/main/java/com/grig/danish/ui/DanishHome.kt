package com.grig.danish.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun DanishHome(
    viewModel: DanishHomeViewModel,
    navigateToNounLearn: (LearnMode, Boolean) -> Unit,
    navigateToNounPractice: (LearnMode, Boolean) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val ttsSettings by viewModel.ttsSettings.collectAsState()
    val availableVoices by viewModel.availableVoices.collectAsState()
    val showTtsSettings by viewModel.showTtsSettings.collectAsState()

    if (showTtsSettings) {
        TtsSettingsDialog(
            settings = ttsSettings,
            availableVoices = availableVoices,
            onSettingsChanged = { viewModel.updateTtsSettings(it) },
            onDismiss = { viewModel.closeTtsSettings() }
        )
    }

    DanishHomeContent(
        state = state,
        onSettingsClick = { viewModel.openTtsSettings() },
        navigateToNounLearn = navigateToNounLearn,
        navigateToNounPractice = navigateToNounPractice
    )
}
