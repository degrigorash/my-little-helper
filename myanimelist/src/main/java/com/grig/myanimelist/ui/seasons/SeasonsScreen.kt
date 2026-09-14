package com.grig.myanimelist.ui.seasons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.grig.core.theme.AppThemeExtended
import com.grig.myanimelist.R
import com.grig.myanimelist.ui.MalLoading
import com.grig.myanimelist.ui.animeedit.EditAnimeBottomSheet
import com.grig.myanimelist.ui.animeedit.EditAnimeViewModel
import com.grig.myanimelist.ui.common.MalErrorContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeasonsScreen(
    viewModel: SeasonsViewModel,
    navigateBack: () -> Unit,
    navigateToAnimeDetail: (Int) -> Unit,
    onListChanged: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val authorized by viewModel.authorized.collectAsState()
    val watchlistIds by viewModel.watchlistIds.collectAsState()
    val editSheetAnime by viewModel.editSheetAnime.collectAsState()
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
                    title = { Text("Seasons", color = colors.headerText) },
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
            ) {
                when (val s = state) {
                    is SeasonsState.Loading -> MalLoading()
                    is SeasonsState.Empty -> SeasonsEmpty()
                    is SeasonsState.Error -> MalErrorContent(
                        title = "Couldn't load your seasons",
                        description = "MyAnimeList data is temporarily unavailable. Please try again later.",
                        onRetry = viewModel::retry
                    )
                    is SeasonsState.Content -> SeasonsContent(
                        groups = s.groups,
                        onAnimeClick = { data -> navigateToAnimeDetail(data.anime.id) },
                        onAnimeLongClick = if (authorized) {
                            viewModel::onAnimeLongClick
                        } else {
                            { data -> navigateToAnimeDetail(data.anime.id) }
                        },
                        watchlistIds = watchlistIds
                    )
                }
            }
        }
    }

    editSheetAnime?.let { data ->
        val editViewModel: EditAnimeViewModel = hiltViewModel()
        EditAnimeBottomSheet(
            data = data,
            viewModel = editViewModel,
            onDismiss = viewModel::dismissEditSheet,
            onSaved = { event ->
                viewModel.onAnimeUpdated(data.anime.id, event.updatedStatus)
                onListChanged()
            },
            onDeleted = {
                viewModel.onAnimeDeleted(data.anime.id)
                onListChanged()
            },
            onOpenDetail = {
                viewModel.dismissEditSheet()
                navigateToAnimeDetail(data.anime.id)
            }
        )
    }
}
