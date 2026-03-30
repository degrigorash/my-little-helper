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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppThemeExtended
import com.grig.core.theme.DanishTheme
import com.grig.danish.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DanishHomeContent(
    state: DanishHomeState,
    onSettingsClick: () -> Unit,
    navigateToNounLearn: (LearnMode) -> Unit,
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
                    title = { Text("Danish", color = colors.headerText) },
                    actions = {
                        IconButton(onClick = onSettingsClick) {
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
            when (state) {
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
                            text = state.message,
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
                                wordCount = state.nounCount,
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

@Preview(name = "Light - Content")
@Composable
private fun DanishHomeContentPreview() {
    DanishTheme(darkTheme = false) {
        DanishHomeContent(
            state = DanishHomeState.Content(nounCount = 42, nounFolders = listOf("default")),
            onSettingsClick = {},
            navigateToNounLearn = {}
        )
    }
}

@Preview(name = "Dark - Content")
@Composable
private fun DanishHomeContentDarkPreview() {
    DanishTheme(darkTheme = true) {
        DanishHomeContent(
            state = DanishHomeState.Content(nounCount = 42, nounFolders = listOf("default")),
            onSettingsClick = {},
            navigateToNounLearn = {}
        )
    }
}

@Preview(name = "Light - Loading")
@Composable
private fun DanishHomeLoadingPreview() {
    DanishTheme(darkTheme = false) {
        DanishHomeContent(
            state = DanishHomeState.Loading,
            onSettingsClick = {},
            navigateToNounLearn = {}
        )
    }
}

@Preview(name = "Dark - Loading")
@Composable
private fun DanishHomeLoadingDarkPreview() {
    DanishTheme(darkTheme = true) {
        DanishHomeContent(
            state = DanishHomeState.Loading,
            onSettingsClick = {},
            navigateToNounLearn = {}
        )
    }
}

@Preview(name = "Light - Error")
@Composable
private fun DanishHomeErrorPreview() {
    DanishTheme(darkTheme = false) {
        DanishHomeContent(
            state = DanishHomeState.Error("Failed to load word database"),
            onSettingsClick = {},
            navigateToNounLearn = {}
        )
    }
}

@Preview(name = "Dark - Error")
@Composable
private fun DanishHomeErrorDarkPreview() {
    DanishTheme(darkTheme = true) {
        DanishHomeContent(
            state = DanishHomeState.Error("Failed to load word database"),
            onSettingsClick = {},
            navigateToNounLearn = {}
        )
    }
}
