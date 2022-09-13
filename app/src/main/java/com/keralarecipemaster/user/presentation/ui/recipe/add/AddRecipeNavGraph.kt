package com.keralarecipemaster.user.presentation.ui.recipe.add

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.keralarecipemaster.user.presentation.viewmodel.AddRecipeViewModel
import com.keralarecipemaster.user.presentation.viewmodel.AuthenticationViewModel

@Composable
fun AddOrEditRecipeNavHost(
    addRecipeViewModel: AddRecipeViewModel,
    authenticationViewModel: AuthenticationViewModel,
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
                authenticationViewModel = authenticationViewModel,
                actionType = "add"
            )
        }
    }
}
