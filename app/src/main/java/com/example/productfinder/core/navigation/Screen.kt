package com.example.itemfinder.core.navigation

sealed class Screen(val route:String)  {
    data object HomeScreen:Screen(route = NavPath.HomeScreen.name)
    data object ItemFounderScreen:Screen(route = NavPath.ItemFoundScreen.name)
    data object OpenInWebViewScreen:Screen(route = "${NavPath.OpenInWebViewScreen.name}/{url}")
}