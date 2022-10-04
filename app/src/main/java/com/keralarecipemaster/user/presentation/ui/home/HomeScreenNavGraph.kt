package com.keralarecipemaster.user.presentation.ui.home

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.keralarecipemaster.user.prefsstore.AuthenticationState
import com.keralarecipemaster.user.presentation.ui.authentication.UserProfileScreen
import com.keralarecipemaster.user.presentation.ui.recipe.RecipesScreen
import com.keralarecipemaster.user.presentation.ui.recipe.add.AddRecipeActivity
import com.keralarecipemaster.user.presentation.ui.reciperequests.RecipeRequestsScreen
import com.keralarecipemaster.user.presentation.viewmodel.AuthenticationViewModel
import com.keralarecipemaster.user.presentation.viewmodel.LocationNotificationViewModel
import com.keralarecipemaster.user.presentation.viewmodel.RecipeListViewModel
import com.keralarecipemaster.user.presentation.viewmodel.RecipeRequestViewModel
import com.keralarecipemaster.user.utils.UserType

@Composable
fun RecipeNavHost(
    recipeListViewModel: RecipeListViewModel,
    authenticationViewModel: AuthenticationViewModel,
    recipeRequestViewModel: RecipeRequestViewModel,
    locationNotificationViewModel: LocationNotificationViewModel,
    navController: NavHostController,
    authenticationState: AuthenticationState
) {
    val context = LocalContext.current
    NavHost(navController = navController, startDestination = if (authenticationState == AuthenticationState.AUTHENTICATED_RESTAURANT_OWNER) HomeItems.MyRequests.name else HomeItems.Famous.name) {
        composable(HomeItems.Famous.name) {
            RecipesScreen(
                authenticationViewModel = authenticationViewModel,
                onFabClick = null,
                recipeViewModel = recipeListViewModel,
                userType = UserType.OWNER,
                navController = navController
            )
        }
        composable(HomeItems.MyRecipes.name) {
            RecipesScreen(
                authenticationViewModel = authenticationViewModel,
                recipeViewModel = recipeListViewModel,
                onFabClick =
                {
                    context.startActivity(Intent(context, AddRecipeActivity::class.java))
                },
                userType = UserType.USER,
                navController = navController
            )
        }
        composable(HomeItems.MyRequests.name) {
            RecipeRequestsScreen(
                recipeRequestViewModel = recipeRequestViewModel,
                navController = navController,
                authenticationState = authenticationState
            )
        }
        composable(HomeItems.Account.name) {
            UserProfileScreen(
                authenticationViewModel = authenticationViewModel,
                locationNotificationViewModel = locationNotificationViewModel
            )
        }
    }
}
