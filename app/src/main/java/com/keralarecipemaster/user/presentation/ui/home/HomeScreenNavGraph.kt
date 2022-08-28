package com.keralarecipemaster.user.presentation.ui.home

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.keralarecipemaster.user.presentation.ui.authentication.UserProfileScreen
import com.keralarecipemaster.user.presentation.ui.recipe.RecipesScreen
import com.keralarecipemaster.user.presentation.ui.recipe.add.AddRecipeActivity
import com.keralarecipemaster.user.presentation.viewmodel.AuthenticationViewModel
import com.keralarecipemaster.user.presentation.viewmodel.RecipeListViewModel
import com.keralarecipemaster.user.utils.UserType

@Composable
fun RecipeNavHost(
    recipeListViewModel: RecipeListViewModel,
    authenticationViewModel: AuthenticationViewModel,
    navController: NavHostController
) {
    val context = LocalContext.current
    NavHost(navController = navController, startDestination = HomeItems.Famous.name) {
        composable(HomeItems.Famous.name) {
            RecipesScreen(
                authenticationViewModel = authenticationViewModel,
                onFabClick =
                {
                    context.startActivity(Intent(context, AddRecipeActivity::class.java))
                },
                recipeViewModel = recipeListViewModel,
                userType = UserType.ADMIN,
                navController = navController
            )
        }
        composable(HomeItems.UserAdded.name) {
            RecipesScreen(
                authenticationViewModel = authenticationViewModel,
                recipeViewModel = recipeListViewModel,
                userType = UserType.USER,
                navController = navController
            )
        }
        composable(HomeItems.Account.name) {
            UserProfileScreen(authenticationViewModel)
        }
    }
}
