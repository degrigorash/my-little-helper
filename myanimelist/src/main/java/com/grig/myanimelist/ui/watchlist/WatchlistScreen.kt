package com.grig.myanimelist.ui.watchlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.grig.myanimelist.R
import com.grig.myanimelist.ui.MalLoading
import com.grig.myanimelist.ui.animeedit.EditAnimeBottomSheet
import com.grig.myanimelist.ui.animeedit.EditAnimeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistScreen(
    viewModel: WatchlistViewModel,
    navigateBack: () -> Unit,
    navigateToAnimeDetail: (Int) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val editSheetAnime by viewModel.editSheetAnime.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Watch Next") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val s = state) {
                is WatchlistState.Loading -> MalLoading()
                is WatchlistState.Empty -> WatchlistEmpty()
                is WatchlistState.Error -> WatchlistErrorContent(onRetry = viewModel::loadWatchlist)
                is WatchlistState.Content -> WatchlistList(
                    items = s.items,
                    onItemClick = viewModel::onItemClick
                )
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
            },
            onDeleted = {
                viewModel.onAnimeDeleted(data.anime.id)
            },
            onOpenDetail = {
                viewModel.dismissEditSheet()
                navigateToAnimeDetail(data.anime.id)
            }
        )
    }
}

@Composable
fun WatchlistList(
    items: List<WatchlistItemData>,
    onItemClick: (WatchlistItemData) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items, key = { it.anime.id }) { item ->
            WatchlistCard(
                item = item,
                onClick = { onItemClick(item) }
            )
        }
    }
}
