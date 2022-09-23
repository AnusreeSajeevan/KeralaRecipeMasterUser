package com.keralarecipemaster.user.presentation.ui.authentication

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.keralarecipemaster.user.presentation.viewmodel.AuthenticationViewModel

@Composable
fun AuthenticationNavHost(
    authenticationViewModel: AuthenticationViewModel,
    isFromProfileScreen: Boolean = false,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = AuthenticationDestinations.Login.name
    ) {
        composable(AuthenticationDestinations.Login.name) {
            LoginScreen(
                authenticationViewModel = authenticationViewModel,
                isFromProfileScreen = isFromProfileScreen,
                navController = navController
            )
        }

        composable(AuthenticationDestinations.RegisterUser.name) {
            ShowUserRegistrationScreen(authenticationViewModel)
        }

        composable(AuthenticationDestinations.RegisterRestaurantOwner.name) {
            ShowRestaurantOwnerRegistrationScreen()
        }
    }
}
