package com.keralarecipemaster.user.presentation.ui.recipe.details

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.keralarecipemaster.user.presentation.ui.recipe.add.AddRecipeDestinations
import com.keralarecipemaster.user.presentation.ui.recipe.add.AddOrEditRecipeScreen
import com.keralarecipemaster.user.presentation.viewmodel.AddRecipeViewModel
import com.keralarecipemaster.user.presentation.viewmodel.RecipeDetailsViewModel

@Composable
fun RecipeDetailsNavHost(
    navController: NavHostController,
    recipeDetailsViewModel: RecipeDetailsViewModel,
    recipeId: Int,
    addRecipeViewModel: AddRecipeViewModel
) {
    NavHost(
        navController = navController,
        startDestination = RecipeDetailsDestinations.RecipeDetails.name
    ) {
        composable(RecipeDetailsDestinations.RecipeDetails.name) {
            RecipeDetailsScreen(
                navController = navController,
                recipeDetailsViewModel = recipeDetailsViewModel,
                recipeId = recipeId
            )
        }

        composable(AddRecipeDestinations.AddRecipeDetails.name) {
            AddOrEditRecipeScreen(addRecipeViewModel = addRecipeViewModel, navController = navController, actionType = "edit", recipeId = recipeId)
        }
    }
}
