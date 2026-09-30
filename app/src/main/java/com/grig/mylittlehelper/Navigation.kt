package com.grig.mylittlehelper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.grig.myanimelist.MalRoute
import com.grig.myanimelist.malNavigation
import kotlinx.coroutines.flow.Flow

/**
 * @param deepLinks back stacks to push on top of the current screen, one per opened link.
 */
@Composable
fun MyLittleHelperNavHost(
    startDestination: MalRoute,
    deepLinks: Flow<List<MalRoute>>,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        malNavigation(navController)
    }

    LaunchedEffect(navController) {
        deepLinks.collect { routes ->
            routes.forEach { navController.navigate(it) }
        }
    }
}
