package com.keralarecipemaster.admin.presentation.ui.recipe.add

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.keralarecipemaster.admin.presentation.ui.view.PickRestaurantScreen
import com.keralarecipemaster.admin.presentation.viewmodel.AddRecipeViewModel

@Composable
fun AddRecipeNavHost(addRecipeViewModel: AddRecipeViewModel, navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AddRecipeDestinations.AddRecipeDetails.name
    ) {
        composable(AddRecipeDestinations.AddRecipeDetails.name) {
            AddRecipeScreen(
                navController = navController,
                viewModel = addRecipeViewModel
            )
        }
        composable(AddRecipeDestinations.AddRestaurant.name) {
            PickRestaurantScreen(addRecipeViewModel)
        }
    }
}
