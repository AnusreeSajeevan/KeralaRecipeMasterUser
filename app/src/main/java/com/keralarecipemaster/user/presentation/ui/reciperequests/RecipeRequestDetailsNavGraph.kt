package com.keralarecipemaster.user.presentation.ui.reciperequests

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.keralarecipemaster.user.presentation.ui.recipe.details.RecipeDetailsDestinations
import com.keralarecipemaster.user.presentation.viewmodel.RecipeRequestDetailsViewModel

@Composable
fun RecipeRequestDetailsNavHost(
    navController: NavHostController,
    recipeRequestDetailsViewModel: RecipeRequestDetailsViewModel,
    recipeId: Int
) {
    NavHost(
        navController = navController,
        startDestination = RecipeDetailsDestinations.RecipeDetails.name
    ) {
        composable(RecipeDetailsDestinations.RecipeDetails.name) {
            RecipeRequestDetailsScreen(
                navController = navController,
                recipeRequestDetailsViewModel = recipeRequestDetailsViewModel,
                requestId = recipeId
            )
        }
    }
}
