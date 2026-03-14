package com.grig.myanimelist.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.grig.myanimelist.data.model.MalUserState
import com.grig.myanimelist.ui.MalEmpty
import com.grig.myanimelist.ui.MalError
import com.grig.myanimelist.ui.MalLoading
import com.grig.myanimelist.ui.animeedit.EditAnimeBottomSheet
import com.grig.myanimelist.ui.animeedit.EditAnimeViewModel

@Composable
fun MalHomeScreen(
    modifier: Modifier = Modifier,
    viewModel: MalHomeViewModel,
    navigateToLogin: () -> Unit
) {
    val userState by viewModel.malUserFlow.collectAsState(initial = MalUserState.Unauthorized)
    val activeTab by viewModel.activeTab.collectAsState()
    val animeFilter by viewModel.animeFilter.collectAsState()
    val mangaFilter by viewModel.mangaFilter.collectAsState()
    val listState by viewModel.listState.collectAsState()
    val guestUsername by viewModel.guestUsername.collectAsState()
    val editSheetAnime by viewModel.editSheetAnime.collectAsState()
    val authorized = userState is MalUserState.Authorized
    val user = (userState as? MalUserState.Authorized)?.user

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        UserHeader(
            user = user,
            authorized = authorized,
            guestUsername = guestUsername,
            onGuestUsernameChange = viewModel::setGuestUsername,
            onGuestSearch = viewModel::searchGuestList,
            onLogoutClick = {
                viewModel.malLogout()
                navigateToLogin()
            }
        )

        TabToggle(
            activeTab = activeTab,
            onTabSelected = { viewModel.selectTab(it) }
        )

        FilterChipsRow(
            activeTab = activeTab,
            animeFilter = animeFilter,
            mangaFilter = mangaFilter,
            onAnimeFilterSelected = { viewModel.selectAnimeFilter(it) },
            onMangaFilterSelected = { viewModel.selectMangaFilter(it) }
        )

        Box(modifier = Modifier.weight(1f).navigationBarsPadding()) {
            when (val state = listState) {
                is ListState.Loading -> MalLoading()
                is ListState.Empty -> MalEmpty(activeTab = activeTab)
                is ListState.Error -> MalError(
                    activeTab = activeTab,
                    exception = state.exception,
                    onRetry = { viewModel.retry() }
                )
                is ListState.AnimeContent -> AnimeList(
                    animes = state.animes,
                    onAnimeClick = if (authorized) viewModel::onAnimeClick else null
                )
                is ListState.MangaContent -> MangaList(mangas = state.mangas)
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
            }
        )
    }
}
