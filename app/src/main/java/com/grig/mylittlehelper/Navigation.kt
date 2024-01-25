package com.grig.mylittlehelper

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.grig.mylittlehelper.ui.home.HomeScreen

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
            HomeScreen(
                // viewModel = viewModel,
                // navigateToPhoto = {
                //     navController.navigate(Screens.Photo.toString())
                // },
                // navigateToManagePhotos = {
                //     navController.navigate("${Screens.ManagePhotos}/$it")
                // }
            )
        }
    }
}