package com.grig.mylittlehelper

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.grig.myanimelist.MalRoute
import com.grig.myanimelist.malNavigation

@Composable
fun MyLittleHelperNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = MalRoute.MalLogin
    ) {
        malNavigation(navController)
    }
}
