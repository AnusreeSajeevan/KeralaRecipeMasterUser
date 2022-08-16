package com.keralarecipemaster.admin.presentation.ui

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.keralarecipemaster.admin.presentation.ui.recipe.add.AddRecipeActivity
import com.keralarecipemaster.admin.presentation.ui.home.HomeItems
import com.keralarecipemaster.admin.presentation.viewmodel.RecipeListViewModel
import com.keralarecipemaster.admin.utils.UserType

@Composable
fun RecipeNavHost(viewModel: RecipeListViewModel, navController: NavHostController) {
    NavHost(navController = navController, startDestination = HomeItems.Famous.name) {
        composable(HomeItems.Famous.name) {
            val context = LocalContext.current
            RecipesScreen(
                onFabClick =
                {
                    context.startActivity(Intent(context, AddRecipeActivity::class.java))
                },
                recipeViewModel = viewModel,
                userType = UserType.ADMIN
            )
        }
        composable(HomeItems.UserAdded.name) {
            RecipesScreen(
                recipeViewModel = viewModel,
                userType = UserType.USER
            )
        }
        composable(HomeItems.Account.name) {
        }
    }
}
