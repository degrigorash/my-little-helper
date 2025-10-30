package com.grig.mylittlehelper

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.grig.danish.DanishRoute
import com.grig.danish.danishNavigation
import com.grig.myanimelist.MalRoute
import com.grig.myanimelist.malNavigation
import com.grig.mylittlehelper.ui.home.HomeScreen
import com.grig.mylittlehelper.ui.theme.MyLittleHelperTheme

@Composable
fun MyLittleHelperNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Route.Home
    ) {
        composable<Route.Home> {
            MyLittleHelperTheme {
                HomeScreen(
                    navigateToMal = {
                        navController.navigate(MalRoute.MalHome)
                    },
                    navigateToDanish = {
                        navController.navigate(DanishRoute.DanishHome)
                    }
                )
            }
        }
        malNavigation(navController)
        danishNavigation()
    }
}