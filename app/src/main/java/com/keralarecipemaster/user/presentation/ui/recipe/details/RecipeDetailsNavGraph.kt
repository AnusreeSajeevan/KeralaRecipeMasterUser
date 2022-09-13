package com.keralarecipemaster.user.presentation.ui.recipe.details

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.keralarecipemaster.user.presentation.ui.recipe.add.AddOrEditRecipeScreen
import com.keralarecipemaster.user.presentation.ui.recipe.add.AddRecipeDestinations
import com.keralarecipemaster.user.presentation.viewmodel.AddRecipeViewModel
import com.keralarecipemaster.user.presentation.viewmodel.AuthenticationViewModel
import com.keralarecipemaster.user.presentation.viewmodel.RecipeDetailsViewModel

@Composable
fun RecipeDetailsNavHost(
    recipeId: Int,
    recipeDetailsViewModel: RecipeDetailsViewModel,
    addRecipeViewModel: AddRecipeViewModel,
    authenticationViewModel: AuthenticationViewModel,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = RecipeDetailsDestinations.RecipeDetails.name
    ) {
        composable(RecipeDetailsDestinations.RecipeDetails.name) {
            RecipeDetailsScreen(
                recipeId = recipeId,
                recipeDetailsViewModel = recipeDetailsViewModel,
                authenticationViewModel = authenticationViewModel,
                navController = navController
            )
        }

        composable(AddRecipeDestinations.AddRecipeDetails.name) {
            AddOrEditRecipeScreen(
                addRecipeViewModel = addRecipeViewModel,
                authenticationViewModel = authenticationViewModel,
                navController = navController,
                actionType = "edit",
                recipeId = recipeId
            )
        }
    }
}
