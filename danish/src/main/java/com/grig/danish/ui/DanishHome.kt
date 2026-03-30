package com.grig.danish.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppThemeExtended
import com.grig.danish.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DanishHome(
    viewModel: DanishHomeViewModel,
    navigateToNounLearn: (LearnMode) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val ttsSettings by viewModel.ttsSettings.collectAsState()
    val availableVoices by viewModel.availableVoices.collectAsState()
    val showTtsSettings by viewModel.showTtsSettings.collectAsState()
    val colors = AppThemeExtended.colorScheme

    if (showTtsSettings) {
        TtsSettingsDialog(
            settings = ttsSettings,
            availableVoices = availableVoices,
            onSettingsChanged = { viewModel.updateTtsSettings(it) },
            onDismiss = { viewModel.closeTtsSettings() }
        )
    }

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
                    title = { Text("Danish", color = colors.headerText) },
                    actions = {
                        IconButton(onClick = { viewModel.openTtsSettings() }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_settings),
                                contentDescription = "Voice settings",
                                tint = colors.headerText
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
                is DanishHomeState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is DanishHomeState.Error -> {
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

                is DanishHomeState.Content -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            CategoryCard(
                                title = "Nouns",
                                wordCount = current.nounCount,
                                enabled = true,
                                onLearnClick = navigateToNounLearn
                            )
                        }
                        item {
                            CategoryCard(
                                title = "Verbs",
                                wordCount = 0,
                                enabled = false,
                                onLearnClick = {}
                            )
                        }
                        item {
                            CategoryCard(
                                title = "Adjectives",
                                wordCount = 0,
                                enabled = false,
                                onLearnClick = {}
                            )
                        }
                    }
                }
            }
        }
    }
}
