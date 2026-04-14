package com.grig.myanimelist.ui.animelist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.grig.core.theme.AppThemeExtended
import com.grig.myanimelist.data.model.anime.MalAnimeWatchingStatus
import com.grig.myanimelist.ui.MalEmpty
import com.grig.myanimelist.ui.MalError
import com.grig.myanimelist.ui.MalLoading
import com.grig.myanimelist.ui.animeedit.EditAnimeBottomSheet
import com.grig.myanimelist.ui.animeedit.EditAnimeViewModel
import com.grig.myanimelist.ui.home.MalTab
import com.grig.myanimelist.ui.home.UpcomingFilterButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeListPage(
    viewModel: AnimeListViewModel,
    authorized: Boolean,
    onOpenDetail: (Int) -> Unit
) {
    val listState by viewModel.listState.collectAsState()
    val statusFilter by viewModel.statusFilter.collectAsState()
    val upcomingFilter by viewModel.upcomingFilter.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val editSheetAnime by viewModel.editSheetAnime.collectAsState()
    val watchlistIds by viewModel.watchlistIds.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        AnimeFilterChipsRow(
            statusFilter = statusFilter,
            upcomingFilter = upcomingFilter,
            onFilterSelected = viewModel::selectFilter,
            onUpcomingToggle = viewModel::toggleUpcomingFilter
        )

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = viewModel::refresh,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            when (val state = listState) {
                is AnimeListState.Loading -> MalLoading()
                is AnimeListState.Empty -> MalEmpty(activeTab = MalTab.Anime)
                is AnimeListState.Error -> MalError(
                    activeTab = MalTab.Anime,
                    exception = state.exception,
                    onRetry = viewModel::retry
                )
                is AnimeListState.Content -> AnimeList(
                    animes = state.animes,
                    onAnimeClick = if (authorized) {
                        viewModel::onAnimeClick
                    } else {
                        { data -> onOpenDetail(data.anime.id) }
                    },
                    searchQuery = searchQuery,
                    onSearchQueryChange = viewModel::onSearchQueryChange,
                    watchlistIds = watchlistIds
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
                onOpenDetail(data.anime.id)
            }
        )
    }
}

@Composable
private fun AnimeFilterChipsRow(
    statusFilter: Set<MalAnimeWatchingStatus>,
    upcomingFilter: Boolean,
    onFilterSelected: (MalAnimeWatchingStatus) -> Unit,
    onUpcomingToggle: () -> Unit
) {
    val colors = AppThemeExtended.colorScheme

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item {
            UpcomingFilterButton(selected = upcomingFilter, onClick = onUpcomingToggle)
        }
        items(MalAnimeWatchingStatus.entries) { status ->
            FilterChip(
                selected = status in statusFilter,
                onClick = { onFilterSelected(status) },
                label = { Text(status.displayName) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = colors.malCardStart,
                    selectedLabelColor = colors.cardText
                )
            )
        }
    }
}

