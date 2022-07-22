package com.keralarecipemaster.admin.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

enum class NavigationItems(
    val icon: ImageVector,
    val route: String,
    val title: String
//    val body: @Composable ((String) -> Unit) -> Unit
) {
    DefaultRecipes(
        icon = Icons.Filled.List,
        route = "default_recipes",
        title = "Default"
    ),
    UserAddedRecipes(
        icon = Icons.Filled.List,
        route = "user_added_recipes",
        title = "User Added"
    ),
    Account(
        icon = Icons.Filled.Face,
        route = "account",
        title = ""
    );
}

@Composable
fun DefaultRecipeBody() {
}
