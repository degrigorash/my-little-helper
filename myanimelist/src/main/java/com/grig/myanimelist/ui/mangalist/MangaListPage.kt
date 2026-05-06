package com.grig.myanimelist.ui.mangalist

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.grig.core.theme.AppThemeExtended
import com.grig.myanimelist.data.model.manga.MalMangaReadingStatus
import com.grig.myanimelist.ui.MalEmpty
import com.grig.myanimelist.ui.MalError
import com.grig.myanimelist.ui.MalLoading
import com.grig.myanimelist.ui.home.MalTab
import com.grig.myanimelist.ui.home.UpcomingFilterButton
import com.grig.myanimelist.ui.mangaedit.EditMangaBottomSheet
import com.grig.myanimelist.ui.mangaedit.EditMangaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangaListPage(
    viewModel: MangaListViewModel,
    authorized: Boolean,
    onOpenDetail: (Int) -> Unit
) {
    val listState by viewModel.listState.collectAsState()
    val statusFilter by viewModel.statusFilter.collectAsState()
    val upcomingFilter by viewModel.upcomingFilter.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val editSheetManga by viewModel.editSheetManga.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        MangaFilterChipsRow(
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
                is MangaListState.Loading -> MalLoading()
                is MangaListState.Empty -> MalEmpty(activeTab = MalTab.Manga)
                is MangaListState.Error -> MalError(
                    activeTab = MalTab.Manga,
                    exception = state.exception,
                    onRetry = viewModel::retry
                )
                is MangaListState.Content -> MangaList(
                    mangas = state.mangas,
                    onMangaClick = if (authorized) {
                        viewModel::onMangaClick
                    } else {
                        { data -> onOpenDetail(data.manga.id) }
                    },
                    onMangaLongClick = { data -> onOpenDetail(data.manga.id) },
                    searchQuery = searchQuery,
                    onSearchQueryChange = viewModel::onSearchQueryChange
                )
            }
        }
    }

    editSheetManga?.let { data ->
        val editViewModel: EditMangaViewModel = hiltViewModel()
        EditMangaBottomSheet(
            data = data,
            viewModel = editViewModel,
            onDismiss = viewModel::dismissEditSheet,
            onSaved = { event ->
                viewModel.onMangaUpdated(data.manga.id, event.updatedStatus)
            },
            onDeleted = {
                viewModel.onMangaDeleted(data.manga.id)
            },
            onOpenDetail = {
                viewModel.dismissEditSheet()
                onOpenDetail(data.manga.id)
            }
        )
    }
}

@Composable
private fun MangaFilterChipsRow(
    statusFilter: Set<MalMangaReadingStatus>,
    upcomingFilter: Boolean,
    onFilterSelected: (MalMangaReadingStatus) -> Unit,
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
        items(MalMangaReadingStatus.entries) { status ->
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

