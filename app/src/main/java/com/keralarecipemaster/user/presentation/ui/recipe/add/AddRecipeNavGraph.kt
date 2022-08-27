package com.keralarecipemaster.user.presentation.ui.recipe.add

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.keralarecipemaster.user.presentation.viewmodel.AddRecipeViewModel

@Composable
fun AddOrEditRecipeNavHost(
    addRecipeViewModel: AddRecipeViewModel,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = AddRecipeDestinations.AddRecipeDetails.name
    ) {
        composable(AddRecipeDestinations.AddRecipeDetails.name) {
            AddOrEditRecipeScreen(
                navController = navController,
                addRecipeViewModel = addRecipeViewModel,
                actionType = "add"
            )
        }
    }
}
