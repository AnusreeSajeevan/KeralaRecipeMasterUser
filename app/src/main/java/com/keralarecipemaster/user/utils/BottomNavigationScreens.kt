package com.keralarecipemaster.user.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavigationScreens(val route: String, val resourceId: String, val icon: ImageVector) {
    object FamousRecipes : BottomNavigationScreens(BottomNavigationItems.FamousRecipes.name, BottomNavigationItems.FamousRecipes.name, Icons.Filled.List)
    object MyRecipes : BottomNavigationScreens(BottomNavigationItems.MyRecipes.name, BottomNavigationItems.MyRecipes.name, Icons.Filled.List)
    object ApprovedRecipes : BottomNavigationScreens(BottomNavigationItems.ApprovedRecipes.name, BottomNavigationItems.ApprovedRecipes.name, Icons.Filled.List)
    object PendingRequests : BottomNavigationScreens(BottomNavigationItems.PendingRequests.name, BottomNavigationItems.PendingRequests.name, Icons.Filled.List)
    object Profile : BottomNavigationScreens(BottomNavigationItems.Profile.name, BottomNavigationItems.Profile.name, Icons.Filled.AccountCircle)
//    object Settings : BottomNavigationScreens(BottomNavigationItems.Settings.name, BottomNavigationItems.Settings.name, Icons.Filled.Settings)
}

enum class BottomNavigationItems(
    val value: String
) {
    FamousRecipes(
        value = "Famous Recipes"
    ),
    MyRecipes(
        value = "My Recipes"
    ),
    ApprovedRecipes(
        value = "Approved Recipes"
    ),
    PendingRequests(
        value = "Pending Requests"
    ),
    Profile(
        value = "Profile"
    );/*,
    Settings(
        value = "Settings"
    );*/

    companion object {
        fun fromRoute(route: String?): BottomNavigationItems =
            when (route?.substringBefore("/")) {
                FamousRecipes.name -> FamousRecipes
                MyRecipes.name -> MyRecipes
                Profile.name -> Profile
                ApprovedRecipes.name -> ApprovedRecipes
                PendingRequests.name -> PendingRequests
//                Settings.name -> Settings
                null -> FamousRecipes
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}
