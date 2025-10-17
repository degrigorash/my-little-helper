package com.grig.mylittlehelper

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.grig.danish.ui.danishNavigation
import com.grig.myanimelist.ui.malNavigation
import com.grig.mylittlehelper.ui.home.HomeScreen
import com.grig.mylittlehelper.ui.home.MalHomeScreen
import com.grig.mylittlehelper.ui.theme.MyLittleHelperTheme

@Composable
fun MyLittleHelperNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "home",
    saveStartScreen: (String) -> Unit = {}
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable("home") {
            MyLittleHelperTheme {
                HomeScreen(
                    navigateToMal = {
                        saveStartScreen("mal_home")
                        navController.navigate("mal_home")
                    },
                    navigateToDanish = {
                        saveStartScreen("danish_home")
                        navController.navigate("danish_home")
                    }
                )
            }
        }
        // probably need to move it to mal module
        composable("mal_home") {
            MyLittleHelperTheme {
                MalHomeScreen(
                    viewModel = hiltViewModel(),
                    navigateToAnimeList = { username ->
                        navController.navigate("mal_anime_list/$username")
                    },
                    navigateToMangaList = { username ->
                        navController.navigate("mal_manga_list/$username")
                    }
                )
            }
        }
        malNavigation()
        danishNavigation()
    }
}