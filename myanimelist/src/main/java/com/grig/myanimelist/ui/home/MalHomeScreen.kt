package com.grig.myanimelist.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.grig.myanimelist.data.model.MalUserState
import com.grig.myanimelist.ui.animelist.AnimeListPage
import com.grig.myanimelist.ui.animelist.AnimeListViewModel
import com.grig.myanimelist.ui.mangalist.MangaListPage
import com.grig.myanimelist.ui.mangalist.MangaListViewModel
import kotlinx.coroutines.launch

@Composable
fun MalHomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: MalHomeViewModel,
    animeListViewModel: AnimeListViewModel,
    mangaListViewModel: MangaListViewModel,
    navigateToLogin: () -> Unit,
    navigateToAnimeSearch: () -> Unit,
    navigateToMangaSearch: () -> Unit,
    navigateToAnimeDetail: (Int) -> Unit,
    navigateToMangaDetail: (Int) -> Unit
) {
    val userState by homeViewModel.malUserFlow.collectAsState(initial = MalUserState.Unauthorized)
    val activeTab by homeViewModel.activeTab.collectAsState()
    val guestUsername by homeViewModel.guestUsername.collectAsState()
    val authorized = userState is MalUserState.Authorized
    val user = (userState as? MalUserState.Authorized)?.user

    LaunchedEffect(guestUsername) {
        animeListViewModel.setGuestUsername(guestUsername)
        mangaListViewModel.setGuestUsername(guestUsername)
    }

    val pagerState = rememberPagerState(
        initialPage = MalTab.entries.indexOf(activeTab)
    ) { MalTab.entries.size }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            homeViewModel.selectTab(MalTab.entries[page])
        }
    }

    LaunchedEffect(activeTab) {
        val targetPage = MalTab.entries.indexOf(activeTab)
        if (pagerState.currentPage != targetPage) {
            pagerState.animateScrollToPage(targetPage)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        UserHeader(
            user = user,
            authorized = authorized,
            guestUsername = guestUsername,
            onGuestUsernameChange = homeViewModel::setGuestUsername,
            onGuestSearch = {
                animeListViewModel.searchGuestList()
                mangaListViewModel.searchGuestList()
            },
            onSearchClick = {
                when (activeTab) {
                    MalTab.Anime -> navigateToAnimeSearch()
                    MalTab.Manga -> navigateToMangaSearch()
                }
            },
            onLogoutClick = {
                homeViewModel.malLogout()
                navigateToLogin()
            }
        )

        TabToggle(
            activeTab = activeTab,
            onTabSelected = { tab ->
                homeViewModel.selectTab(tab)
                coroutineScope.launch {
                    pagerState.animateScrollToPage(MalTab.entries.indexOf(tab))
                }
            }
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .navigationBarsPadding()
        ) { page ->
            when (page) {
                0 -> AnimeListPage(
                    viewModel = animeListViewModel,
                    authorized = authorized,
                    onOpenDetail = navigateToAnimeDetail
                )
                1 -> MangaListPage(
                    viewModel = mangaListViewModel,
                    authorized = authorized,
                    onOpenDetail = navigateToMangaDetail
                )
            }
        }
    }
}
