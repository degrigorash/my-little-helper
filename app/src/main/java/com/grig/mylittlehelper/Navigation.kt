package com.grig.mylittlehelper

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.grig.mylittlehelper.ui.home.HomeScreen
import com.grig.mylittlehelper.ui.home.HomeViewModel

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
                viewModel = hiltViewModel(),
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