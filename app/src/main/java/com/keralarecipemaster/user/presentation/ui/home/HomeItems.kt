package com.keralarecipemaster.user.presentation.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class HomeItems(
    val icon: ImageVector,
    val selectedIcon: ImageVector
) {
    FamousRecipes(
        icon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home
    ),
    MyRecipes(
        icon = Icons.Outlined.Menu,
        selectedIcon = Icons.Filled.Menu
    ),
    MyRequests(
        icon = Icons.Outlined.Menu,
        selectedIcon = Icons.Filled.Menu
    ),
    Account(
        icon = Icons.Outlined.Person,
        selectedIcon = Icons.Filled.Person
    );

    companion object {
        fun fromRoute(route: String?): HomeItems =
            when (route?.substringBefore("/")) {
                FamousRecipes.name -> FamousRecipes
                MyRecipes.name -> MyRecipes
                Account.name -> Account
                MyRequests.name -> MyRequests
                null -> FamousRecipes
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}
