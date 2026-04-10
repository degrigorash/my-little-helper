package com.grig.myanimelist.ui.mangadetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppThemeExtended
import com.grig.myanimelist.R
import com.grig.myanimelist.ui.mangasearch.MangaDetailContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangaDetailScreen(
    viewModel: MangaDetailViewModel,
    navigateBack: () -> Unit,
    onListChanged: () -> Unit = {},
    navigateToMangaDetail: (Int) -> Unit = {},
    navigateToAnimeDetail: (Int) -> Unit = {},
    navigateToAuthorDetail: (Int) -> Unit = {},
    navigateToReviews: (Int) -> Unit = {},
    navigateToCharacters: (Int) -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val colors = AppThemeExtended.colorScheme
    val authorized by produceState(initialValue = false) {
        value = viewModel.isAuthorized()
    }

    LaunchedEffect(Unit) {
        viewModel.state.collect { s ->
            if (s is MangaDetailState.Content && s.listChanged) {
                onListChanged()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(colors.gradientBackgroundTop, colors.gradientBackgroundBottom)))
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = navigateBack) {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow_back),
                                contentDescription = "Back",
                                tint = colors.headerText
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = colors.malCardStart.copy(alpha = 0.12f)
                        )
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .navigationBarsPadding()
            ) {
                when (val currentState = state) {
                    is MangaDetailState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(48.dp)
                                .align(Alignment.Center)
                        )
                    }
                    is MangaDetailState.Content -> {
                        MangaDetailContent(
                            manga = currentState.manga,
                            authorized = authorized,
                            isInMyList = currentState.isInMyList,
                            isUpdatingList = currentState.isUpdatingList,
                            onAddToList = viewModel::addToMyList,
                            onDeleteFromList = viewModel::deleteFromMyList,
                            onAuthorClick = navigateToAuthorDetail,
                            onRelatedMangaClick = navigateToMangaDetail,
                            relatedAnime = currentState.relatedAnime,
                            isLoadingRelatedAnime = currentState.isLoadingRelatedAnime,
                            onRelatedAnimeClick = navigateToAnimeDetail,
                            onReviewsClick = { navigateToReviews(currentState.manga.id) },
                            onCharactersClick = { navigateToCharacters(currentState.manga.id) }
                        )
                    }
                    is MangaDetailState.Error -> {
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
    }
}
