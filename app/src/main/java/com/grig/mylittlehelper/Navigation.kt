package com.grig.mylittlehelper

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.grig.myanimelist.ui.malNavigation
import com.grig.mylittlehelper.ui.home.HomeScreen
import com.grig.mylittlehelper.ui.home.HomeViewModel
import com.grig.mylittlehelper.ui.theme.MyLittleHelperTheme

@Composable
fun MyLittleHelperNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "home"
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable("home") {
            MyLittleHelperTheme {
                HomeScreen(
                    viewModel = hiltViewModel(),
                    navigateToAnimeList = {
                        navController.navigate("mal_list")
                    },
                    // navigateToManagePhotos = {
                    //     navController.navigate("${Screens.ManagePhotos}/$it")
                    // }
                )
            }
        }
        malNavigation()
    }
}