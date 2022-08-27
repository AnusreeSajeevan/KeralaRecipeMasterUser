package com.keralarecipemaster.user.presentation.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class HomeItems(
    val icon: ImageVector,
    val selectedIcon: ImageVector
) {
    Famous(
        icon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home
    ),
    UserAdded(
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
                Famous.name -> Famous
                UserAdded.name -> UserAdded
                Account.name -> Account
                null -> Famous
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}
