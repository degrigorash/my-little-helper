package com.grig.danish.ui.noun.practice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppThemeExtended
import com.grig.core.theme.DanishTheme
import com.grig.danish.ui.LearnMode
import com.grig.danish.ui.noun.sampleNoun
import com.grig.danish.ui.noun.sampleNounWithAlt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NounPracticeContent(
    state: NounPracticeState,
    onAnswerChanged: (String) -> Unit,
    onCheck: () -> Unit,
    onShowAnswer: () -> Unit,
    onSpeak: () -> Unit,
    onNext: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppThemeExtended.colorScheme

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(colors.gradientBackgroundTop, colors.gradientBackgroundBottom)
                )
            )
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        val titleText = when ((state as? NounPracticeState.Content)?.mode) {
                            LearnMode.DK_TO_EN -> "Practice: DK → EN"
                            else -> "Practice: EN → DK"
                        }
                        Text(titleText, color = colors.headerText)
                    },
                    navigationIcon = {
                        IconButton(onClick = navigateBack) {
                            Text(
                                text = "✕",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = colors.headerText
                            )
                        }
                    },
                    actions = {
                        val contentState = state as? NounPracticeState.Content
                        if (contentState != null && contentState.progress.isNotEmpty()) {
                            Text(
                                text = contentState.progress,
                                style = MaterialTheme.typography.labelLarge,
                                color = colors.headerText,
                                modifier = Modifier.padding(end = 16.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        ) { innerPadding ->
            when (state) {
                is NounPracticeState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is NounPracticeState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                is NounPracticeState.Content -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp)
                    ) {
                        NounPracticeCard(
                            noun = state.noun,
                            mode = state.mode,
                            userAnswer = state.userAnswer,
                            answerResult = state.answerResult,
                            onAnswerChanged = onAnswerChanged,
                            onCheck = onCheck,
                            onShowAnswer = onShowAnswer,
                            onSpeak = onSpeak,
                            modifier = Modifier.align(Alignment.TopCenter)
                        )

                        if (state.answerResult != AnswerResult.UNANSWERED) {
                            Button(
                                onClick = onNext,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter)
                            ) {
                                Text("Next")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "Light - Unanswered")
@Composable
private fun NounPracticeContentUnansweredPreview() {
    DanishTheme(darkTheme = false) {
        NounPracticeContent(
            state = NounPracticeState.Content(
                noun = sampleNoun,
                mode = LearnMode.EN_TO_DK,
                progress = "3 / 42"
            ),
            onAnswerChanged = {},
            onCheck = {},
            onShowAnswer = {},
            onSpeak = {},
            onNext = {},
            navigateBack = {}
        )
    }
}

@Preview(name = "Dark - Unanswered")
@Composable
private fun NounPracticeContentUnansweredDarkPreview() {
    DanishTheme(darkTheme = true) {
        NounPracticeContent(
            state = NounPracticeState.Content(
                noun = sampleNoun,
                mode = LearnMode.EN_TO_DK,
                progress = "3 / 42"
            ),
            onAnswerChanged = {},
            onCheck = {},
            onShowAnswer = {},
            onSpeak = {},
            onNext = {},
            navigateBack = {}
        )
    }
}

@Preview(name = "Light - Correct")
@Composable
private fun NounPracticeContentCorrectPreview() {
    DanishTheme(darkTheme = false) {
        NounPracticeContent(
            state = NounPracticeState.Content(
                noun = sampleNoun,
                mode = LearnMode.EN_TO_DK,
                userAnswer = "en bog",
                answerResult = AnswerResult.CORRECT,
                progress = "3 / 42"
            ),
            onAnswerChanged = {},
            onCheck = {},
            onShowAnswer = {},
            onSpeak = {},
            onNext = {},
            navigateBack = {}
        )
    }
}

@Preview(name = "Dark - Correct")
@Composable
private fun NounPracticeContentCorrectDarkPreview() {
    DanishTheme(darkTheme = true) {
        NounPracticeContent(
            state = NounPracticeState.Content(
                noun = sampleNoun,
                mode = LearnMode.EN_TO_DK,
                userAnswer = "en bog",
                answerResult = AnswerResult.CORRECT,
                progress = "3 / 42"
            ),
            onAnswerChanged = {},
            onCheck = {},
            onShowAnswer = {},
            onSpeak = {},
            onNext = {},
            navigateBack = {}
        )
    }
}

@Preview(name = "Light - Incorrect")
@Composable
private fun NounPracticeContentIncorrectPreview() {
    DanishTheme(darkTheme = false) {
        NounPracticeContent(
            state = NounPracticeState.Content(
                noun = sampleNounWithAlt,
                mode = LearnMode.DK_TO_EN,
                userAnswer = "wrong",
                answerResult = AnswerResult.INCORRECT,
                progress = "7 / 42"
            ),
            onAnswerChanged = {},
            onCheck = {},
            onShowAnswer = {},
            onSpeak = {},
            onNext = {},
            navigateBack = {}
        )
    }
}

@Preview(name = "Dark - Incorrect")
@Composable
private fun NounPracticeContentIncorrectDarkPreview() {
    DanishTheme(darkTheme = true) {
        NounPracticeContent(
            state = NounPracticeState.Content(
                noun = sampleNounWithAlt,
                mode = LearnMode.DK_TO_EN,
                userAnswer = "wrong",
                answerResult = AnswerResult.INCORRECT,
                progress = "7 / 42"
            ),
            onAnswerChanged = {},
            onCheck = {},
            onShowAnswer = {},
            onSpeak = {},
            onNext = {},
            navigateBack = {}
        )
    }
}

@Preview(name = "Light - Loading")
@Composable
private fun NounPracticeLoadingPreview() {
    DanishTheme(darkTheme = false) {
        NounPracticeContent(
            state = NounPracticeState.Loading,
            onAnswerChanged = {},
            onCheck = {},
            onShowAnswer = {},
            onSpeak = {},
            onNext = {},
            navigateBack = {}
        )
    }
}

@Preview(name = "Dark - Loading")
@Composable
private fun NounPracticeLoadingDarkPreview() {
    DanishTheme(darkTheme = true) {
        NounPracticeContent(
            state = NounPracticeState.Loading,
            onAnswerChanged = {},
            onCheck = {},
            onShowAnswer = {},
            onSpeak = {},
            onNext = {},
            navigateBack = {}
        )
    }
}

@Preview(name = "Light - Error")
@Composable
private fun NounPracticeErrorPreview() {
    DanishTheme(darkTheme = false) {
        NounPracticeContent(
            state = NounPracticeState.Error("Failed to load nouns"),
            onAnswerChanged = {},
            onCheck = {},
            onShowAnswer = {},
            onSpeak = {},
            onNext = {},
            navigateBack = {}
        )
    }
}

@Preview(name = "Dark - Error")
@Composable
private fun NounPracticeErrorDarkPreview() {
    DanishTheme(darkTheme = true) {
        NounPracticeContent(
            state = NounPracticeState.Error("Failed to load nouns"),
            onAnswerChanged = {},
            onCheck = {},
            onShowAnswer = {},
            onSpeak = {},
            onNext = {},
            navigateBack = {}
        )
    }
}
