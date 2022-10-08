package com.keralarecipemaster.user.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector
import com.keralarecipemaster.user.presentation.ui.home.HomeItems

sealed class BottomNavigationScreens(val route: String, val resourceId: String, val icon: ImageVector) {
    object FamousRecipes : BottomNavigationScreens(HomeItems.FamousRecipes.name, "Famous Recipes", Icons.Filled.List)
    object MyRecipes : BottomNavigationScreens(HomeItems.MyRecipes.name, "My Recipes", Icons.Filled.List)
    object ApprovedRecipes : BottomNavigationScreens(HomeItems.ApprovedRecipes.name, "Approved Recipes", Icons.Filled.List)
    object PendingRequests : BottomNavigationScreens(HomeItems.PendingRequests.name, "Pending Requests", Icons.Filled.List)
    object Profile : BottomNavigationScreens(HomeItems.Profile.name, "Profile", Icons.Filled.AccountCircle)
}