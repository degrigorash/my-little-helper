package com.grig.danish.ui.noun

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppThemeExtended
import com.grig.danish.ui.LearnMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NounLearnScreen(
    viewModel: NounLearnViewModel,
    navigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val colors = AppThemeExtended.colorScheme

    Box(
        modifier = Modifier
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
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        ) { innerPadding ->
            when (val current = state) {
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
                            text = current.message,
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
                            noun = current.noun,
                            mode = current.mode,
                            revealed = current.revealed,
                            onReveal = { viewModel.reveal() },
                            onSpeak = { viewModel.speakDanish() },
                            modifier = Modifier.align(Alignment.TopCenter)
                        )

                        Button(
                            onClick = { viewModel.next() },
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
