package com.grig.myanimelist

import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.grig.core.theme.AppTheme
import com.grig.myanimelist.ui.animedetail.AnimeDetailScreen
import com.grig.myanimelist.ui.animelist.AnimeListViewModel
import com.grig.myanimelist.ui.animesearch.AnimeSearchScreen
import com.grig.myanimelist.ui.authordetail.AuthorDetailScreen
import com.grig.myanimelist.ui.characterdetail.CharacterDetailScreen
import com.grig.myanimelist.ui.characters.CharactersScreen
import com.grig.myanimelist.ui.home.MalHomeScreen
import com.grig.myanimelist.ui.home.MalHomeViewModel
import com.grig.myanimelist.ui.login.MalLoginScreen
import com.grig.myanimelist.ui.mangadetail.MangaDetailScreen
import com.grig.myanimelist.ui.mangalist.MangaListViewModel
import com.grig.myanimelist.ui.mangasearch.MangaSearchScreen
import com.grig.myanimelist.ui.reviews.ReviewsScreen
import com.grig.myanimelist.ui.studiodetail.StudioDetailScreen

const val ANIME_LIST_CHANGED = "anime_list_changed"
const val MANGA_LIST_CHANGED = "manga_list_changed"

fun NavGraphBuilder.malNavigation(
    navController: NavHostController
) {
    composable<MalRoute.MalLogin> {
        AppTheme {
            MalLoginScreen(
                viewModel = hiltViewModel(),
                navigateToHome = {
                    navController.navigate(MalRoute.MalHome) {
                        popUpTo<MalRoute.MalLogin> { inclusive = true }
                    }
                },
                navigateBack = { navController.popBackStack() }
            )
        }
    }
    composable<MalRoute.MalHome> { backStackEntry ->
        val homeViewModel: MalHomeViewModel = hiltViewModel()
        val animeListViewModel: AnimeListViewModel = hiltViewModel()
        val mangaListViewModel: MangaListViewModel = hiltViewModel()
        val savedStateHandle = backStackEntry.savedStateHandle

        LaunchedEffect(Unit) {
            savedStateHandle.getStateFlow(ANIME_LIST_CHANGED, false).collect { changed ->
                if (changed) {
                    savedStateHandle[ANIME_LIST_CHANGED] = false
                    animeListViewModel.refresh()
                }
            }
        }
        LaunchedEffect(Unit) {
            savedStateHandle.getStateFlow(MANGA_LIST_CHANGED, false).collect { changed ->
                if (changed) {
                    savedStateHandle[MANGA_LIST_CHANGED] = false
                    mangaListViewModel.refresh()
                }
            }
        }

        AppTheme {
            MalHomeScreen(
                homeViewModel = homeViewModel,
                animeListViewModel = animeListViewModel,
                mangaListViewModel = mangaListViewModel,
                navigateToLogin = {
                    navController.navigate(MalRoute.MalLogin) {
                        popUpTo<MalRoute.MalHome> { inclusive = true }
                    }
                },
                navigateToAnimeSearch = {
                    navController.navigate(MalRoute.AnimeSearch)
                },
                navigateToMangaSearch = {
                    navController.navigate(MalRoute.MangaSearch)
                },
                navigateToAnimeDetail = { animeId ->
                    navController.navigate(MalRoute.AnimeDetail(animeId))
                },
                navigateToMangaDetail = { mangaId ->
                    navController.navigate(MalRoute.MangaDetail(mangaId))
                }
            )
        }
    }
    composable<MalRoute.AnimeSearch> { backStackEntry ->
        LaunchedEffect(Unit) {
            backStackEntry.savedStateHandle
                .getStateFlow(ANIME_LIST_CHANGED, false)
                .collect { changed ->
                    if (changed) {
                        backStackEntry.savedStateHandle[ANIME_LIST_CHANGED] = false
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set(ANIME_LIST_CHANGED, true)
                    }
                }
        }

        AppTheme {
            AnimeSearchScreen(
                viewModel = hiltViewModel(),
                navigateBack = { navController.popBackStack() },
                navigateToAnimeDetail = { animeId ->
                    navController.navigate(MalRoute.AnimeDetail(animeId))
                }
            )
        }
    }
    composable<MalRoute.MangaSearch> { backStackEntry ->
        LaunchedEffect(Unit) {
            backStackEntry.savedStateHandle
                .getStateFlow(MANGA_LIST_CHANGED, false)
                .collect { changed ->
                    if (changed) {
                        backStackEntry.savedStateHandle[MANGA_LIST_CHANGED] = false
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set(MANGA_LIST_CHANGED, true)
                    }
                }
        }

        AppTheme {
            MangaSearchScreen(
                viewModel = hiltViewModel(),
                navigateBack = { navController.popBackStack() },
                navigateToMangaDetail = { mangaId ->
                    navController.navigate(MalRoute.MangaDetail(mangaId))
                }
            )
        }
    }
    composable<MalRoute.AnimeDetail> {
        AppTheme {
            AnimeDetailScreen(
                viewModel = hiltViewModel(),
                navigateBack = { navController.popBackStack() },
                onListChanged = {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(ANIME_LIST_CHANGED, true)
                },
                navigateToAnimeDetail = { animeId ->
                    navController.navigate(MalRoute.AnimeDetail(animeId)) {
                        popUpTo<MalRoute.AnimeDetail> { inclusive = true }
                    }
                },
                navigateToMangaDetail = { mangaId ->
                    navController.navigate(MalRoute.MangaDetail(mangaId)) {
                        popUpTo<MalRoute.AnimeDetail> { inclusive = true }
                    }
                },
                navigateToStudioDetail = { studioId ->
                    navController.navigate(MalRoute.StudioDetail(studioId))
                },
                navigateToReviews = { animeId ->
                    navController.navigate(MalRoute.Reviews(animeId, "anime"))
                },
                navigateToCharacters = { animeId ->
                    navController.navigate(MalRoute.Characters(animeId, "anime"))
                }
            )
        }
    }
    composable<MalRoute.MangaDetail> {
        AppTheme {
            MangaDetailScreen(
                viewModel = hiltViewModel(),
                navigateBack = { navController.popBackStack() },
                onListChanged = {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(MANGA_LIST_CHANGED, true)
                },
                navigateToMangaDetail = { mangaId ->
                    navController.navigate(MalRoute.MangaDetail(mangaId)) {
                        popUpTo<MalRoute.MangaDetail> { inclusive = true }
                    }
                },
                navigateToAnimeDetail = { animeId ->
                    navController.navigate(MalRoute.AnimeDetail(animeId)) {
                        popUpTo<MalRoute.MangaDetail> { inclusive = true }
                    }
                },
                navigateToAuthorDetail = { authorId ->
                    navController.navigate(MalRoute.AuthorDetail(authorId))
                },
                navigateToReviews = { mangaId ->
                    navController.navigate(MalRoute.Reviews(mangaId, "manga"))
                },
                navigateToCharacters = { mangaId ->
                    navController.navigate(MalRoute.Characters(mangaId, "manga"))
                }
            )
        }
    }
    composable<MalRoute.Reviews> {
        AppTheme {
            ReviewsScreen(
                viewModel = hiltViewModel(),
                navigateBack = { navController.popBackStack() }
            )
        }
    }
    composable<MalRoute.Characters> {
        AppTheme {
            CharactersScreen(
                viewModel = hiltViewModel(),
                navigateBack = { navController.popBackStack() },
                navigateToCharacterDetail = { characterId ->
                    navController.navigate(MalRoute.CharacterDetail(characterId))
                }
            )
        }
    }
    composable<MalRoute.CharacterDetail> {
        AppTheme {
            CharacterDetailScreen(
                viewModel = hiltViewModel(),
                navigateBack = { navController.popBackStack() }
            )
        }
    }
    composable<MalRoute.StudioDetail> {
        AppTheme {
            StudioDetailScreen(
                viewModel = hiltViewModel(),
                navigateBack = { navController.popBackStack() },
                navigateToAnimeDetail = { animeId ->
                    navController.navigate(MalRoute.AnimeDetail(animeId))
                }
            )
        }
    }
    composable<MalRoute.AuthorDetail> {
        AppTheme {
            AuthorDetailScreen(
                viewModel = hiltViewModel(),
                navigateBack = { navController.popBackStack() },
                navigateToAnimeDetail = { animeId ->
                    navController.navigate(MalRoute.AnimeDetail(animeId))
                },
                navigateToMangaDetail = { mangaId ->
                    navController.navigate(MalRoute.MangaDetail(mangaId))
                }
            )
        }
    }
}
