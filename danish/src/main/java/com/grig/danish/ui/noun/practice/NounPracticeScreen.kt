package com.grig.danish.ui.noun.practice

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun NounPracticeScreen(
    viewModel: NounPracticeViewModel,
    navigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    NounPracticeContent(
        state = state,
        onAnswerChanged = { viewModel.onAnswerChanged(it) },
        onCheck = { viewModel.checkAnswer() },
        onShowAnswer = { viewModel.showAnswer() },
        onSpeak = { viewModel.speakDanish() },
        onNext = { viewModel.next() },
        navigateBack = navigateBack
    )
}
