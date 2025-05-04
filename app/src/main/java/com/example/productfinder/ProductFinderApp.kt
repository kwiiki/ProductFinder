package com.example.productfinder

import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.itemfinder.core.navigation.NavPath
import com.example.productfinder.core.navigation.Screen
import com.example.productfinder.presentation.homescreen.HomeScreen
import com.example.itemfinder.presentation.homescreen.OpenInWebViewScreen
import com.example.productfinder.presentation.filterscreen.FilterScreen
import com.example.productfinder.presentation.homescreen.viewmodel.HomeScreenViewModel
import com.example.productfinder.presentation.itemfoundscreen.ItemFoundScreen

@Composable
fun ProductFinderApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val homeScreenViewModel: HomeScreenViewModel = hiltViewModel()

    Scaffold(
        modifier = Modifier.padding(bottom = 0.dp),
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavPath.FilterScreen.name,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(Screen.HomeScreen.route) {
                HomeScreen(navController = navController, homeScreenViewModel)
            }
            composable(Screen.ItemFounderScreen.route) {
                ItemFoundScreen(navController, homeScreenViewModel)
            }
            composable(
                route = "${NavPath.OpenInWebViewScreen.name}/{url}",
                arguments = listOf(navArgument("url") { type = NavType.StringType })
            ) { backStackEntry ->
                val url = backStackEntry.arguments?.getString("url")?.let { Uri.decode(it) }
                OpenInWebViewScreen(url = url)
            }
            composable(Screen.FilterScreen.route) {
                FilterScreen(navController)
            }
        }
    }
}