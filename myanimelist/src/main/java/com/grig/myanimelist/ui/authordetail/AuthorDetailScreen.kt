package com.grig.myanimelist.ui.authordetail

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
fun AuthorDetailScreen(
    viewModel: AuthorDetailViewModel,
    navigateBack: () -> Unit,
    navigateToAnimeDetail: (Int) -> Unit = {},
    navigateToMangaDetail: (Int) -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val colors = AppThemeExtended.colorScheme

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(colors.gradientBackgroundTop, colors.gradientBackgroundBottom)))
    ) {
        when (val currentState = state) {
            is AuthorDetailState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.Center)
                )
            }
            is AuthorDetailState.Content -> {
                val person = currentState.person
                val imageUrl = person.images?.jpg?.imageUrl

                CollapsingHeaderLayout(
                    title = person.name,
                    imageUrl = imageUrl,
                    onBackClick = navigateBack,
                    onImageClick = {},
                    imageContentScale = ContentScale.Fit
                ) { titleAlpha ->
                    AuthorDetailContent(
                        person = person,
                        onAnimeClick = navigateToAnimeDetail,
                        onMangaClick = navigateToMangaDetail,
                        titleAlpha = titleAlpha
                    )
                }
            }
            is AuthorDetailState.Error -> {
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
