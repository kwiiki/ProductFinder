package com.example.productfinder.core.navigation

import com.example.itemfinder.core.navigation.NavPath

sealed class Screen(val route:String)  {
    data object HomeScreen:Screen(route = NavPath.HomeScreen.name)
    data object ItemFounderScreen:Screen(route = NavPath.ItemFoundScreen.name)
    data object FilterScreen: Screen(route = NavPath.FilterScreen.name)
    data object OpenInWebViewScreen:Screen(route = "${NavPath.OpenInWebViewScreen.name}/{url}")
}