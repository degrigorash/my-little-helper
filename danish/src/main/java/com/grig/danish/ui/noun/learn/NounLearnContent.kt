package com.grig.danish.ui.noun.learn

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
fun NounLearnContent(
    state: NounLearnState,
    onReveal: () -> Unit,
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
                        val titleText = when ((state as? NounLearnState.Content)?.mode) {
                            LearnMode.DK_TO_EN -> "Nouns: DK → EN"
                            else -> "Nouns: EN → DK"
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
                        val contentState = state as? NounLearnState.Content
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
                is NounLearnState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is NounLearnState.Error -> {
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

                is NounLearnState.Content -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp)
                    ) {
                        NounCard(
                            noun = state.noun,
                            mode = state.mode,
                            revealed = state.revealed,
                            onReveal = onReveal,
                            onSpeak = onSpeak,
                            modifier = Modifier.align(Alignment.TopCenter)
                        )

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

@Preview(name = "Light - Content Hidden")
@Composable
private fun NounLearnContentHiddenPreview() {
    DanishTheme(darkTheme = false) {
        NounLearnContent(
            state = NounLearnState.Content(
                noun = sampleNoun,
                mode = LearnMode.EN_TO_DK,
                revealed = false,
                progress = "3 / 42"
            ),
            onReveal = {},
            onSpeak = {},
            onNext = {},
            navigateBack = {}
        )
    }
}

@Preview(name = "Dark - Content Hidden")
@Composable
private fun NounLearnContentHiddenDarkPreview() {
    DanishTheme(darkTheme = true) {
        NounLearnContent(
            state = NounLearnState.Content(
                noun = sampleNoun,
                mode = LearnMode.EN_TO_DK,
                revealed = false,
                progress = "3 / 42"
            ),
            onReveal = {},
            onSpeak = {},
            onNext = {},
            navigateBack = {}
        )
    }
}

@Preview(name = "Light - Content Revealed")
@Composable
private fun NounLearnContentRevealedPreview() {
    DanishTheme(darkTheme = false) {
        NounLearnContent(
            state = NounLearnState.Content(
                noun = sampleNounWithAlt,
                mode = LearnMode.DK_TO_EN,
                revealed = true,
                progress = "7 / 42"
            ),
            onReveal = {},
            onSpeak = {},
            onNext = {},
            navigateBack = {}
        )
    }
}

@Preview(name = "Dark - Content Revealed")
@Composable
private fun NounLearnContentRevealedDarkPreview() {
    DanishTheme(darkTheme = true) {
        NounLearnContent(
            state = NounLearnState.Content(
                noun = sampleNounWithAlt,
                mode = LearnMode.DK_TO_EN,
                revealed = true,
                progress = "7 / 42"
            ),
            onReveal = {},
            onSpeak = {},
            onNext = {},
            navigateBack = {}
        )
    }
}

@Preview(name = "Light - Loading")
@Composable
private fun NounLearnLoadingPreview() {
    DanishTheme(darkTheme = false) {
        NounLearnContent(
            state = NounLearnState.Loading,
            onReveal = {},
            onSpeak = {},
            onNext = {},
            navigateBack = {}
        )
    }
}

@Preview(name = "Dark - Loading")
@Composable
private fun NounLearnLoadingDarkPreview() {
    DanishTheme(darkTheme = true) {
        NounLearnContent(
            state = NounLearnState.Loading,
            onReveal = {},
            onSpeak = {},
            onNext = {},
            navigateBack = {}
        )
    }
}

@Preview(name = "Light - Error")
@Composable
private fun NounLearnErrorPreview() {
    DanishTheme(darkTheme = false) {
        NounLearnContent(
            state = NounLearnState.Error("Failed to load nouns"),
            onReveal = {},
            onSpeak = {},
            onNext = {},
            navigateBack = {}
        )
    }
}

@Preview(name = "Dark - Error")
@Composable
private fun NounLearnErrorDarkPreview() {
    DanishTheme(darkTheme = true) {
        NounLearnContent(
            state = NounLearnState.Error("Failed to load nouns"),
            onReveal = {},
            onSpeak = {},
            onNext = {},
            navigateBack = {}
        )
    }
}
