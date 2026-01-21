package com.example.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.home.HomeScreen
import com.example.presentation.destination.ScreenDestination
import com.example.presentation.screens.SearchScreen.SearchScreen


@Composable
fun NavHostApp(
    navHostController: NavHostController
) {
    NavHost(navController = navHostController,
        startDestination = ScreenDestination.HomeScreen){

        composable<ScreenDestination.HomeScreen>{
            HomeScreen(
                navHostController = navHostController
            )
        }

        composable<ScreenDestination.SearchScreen>{
            SearchScreen(
                navHostController = navHostController
            )
        }
    }
}
