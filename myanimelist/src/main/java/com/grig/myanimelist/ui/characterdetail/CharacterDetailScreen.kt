package com.grig.myanimelist.ui.characterdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppThemeExtended
import com.grig.myanimelist.ui.common.CollapsingHeaderLayout

@Composable
fun CharacterDetailScreen(
    viewModel: CharacterDetailViewModel,
    navigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val colors = AppThemeExtended.colorScheme

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(colors.gradientBackgroundTop, colors.gradientBackgroundBottom)))
    ) {
        when (val currentState = state) {
            is CharacterDetailState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.Center)
                )
            }
            is CharacterDetailState.Content -> {
                val character = currentState.character
                val imageUrl = character.images?.jpg?.imageUrl
                    ?: character.images?.webp?.imageUrl

                CollapsingHeaderLayout(
                    title = character.name,
                    imageUrl = imageUrl,
                    onBackClick = navigateBack,
                    onImageClick = {},
                    imageContentScale = ContentScale.Fit
                ) { titleAlpha ->
                    CharacterDetailContent(
                        character = character,
                        titleAlpha = titleAlpha
                    )
                }
            }
            is CharacterDetailState.Error -> {
                Text(
                    text = currentState.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(32.dp)
                )
            }
        }
    }
}
