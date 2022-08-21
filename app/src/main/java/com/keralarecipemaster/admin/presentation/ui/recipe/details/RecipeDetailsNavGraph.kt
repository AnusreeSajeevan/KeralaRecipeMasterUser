package com.keralarecipemaster.admin.presentation.ui.recipe.details

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.keralarecipemaster.admin.presentation.viewmodel.RecipeDetailsViewModel

@Composable
fun RecipeDetailsNavHost(
    navController: NavHostController,
    recipeDetailsViewModel: RecipeDetailsViewModel,
    recipeId: Int
) {
    NavHost(
        navController = navController,
        startDestination = RecipeDetailsDestinations.RecipeDetails.name
    ) {
        composable(RecipeDetailsDestinations.RecipeDetails.name) {
            RecipeDetailsScreen(recipeDetailsViewModel, recipeId)
        }
    }
}
