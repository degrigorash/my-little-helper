package com.grig.danish.ui.noun.learn

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun NounLearnScreen(
    viewModel: NounLearnViewModel,
    navigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    NounLearnContent(
        state = state,
        onReveal = { viewModel.reveal() },
        onSpeak = { viewModel.speak(it) },
        onNext = { viewModel.next() },
        navigateBack = navigateBack
    )
}
