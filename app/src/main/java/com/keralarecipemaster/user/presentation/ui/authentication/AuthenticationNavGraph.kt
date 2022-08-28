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
            ShowLoginScreen(
                authenticationViewModel = authenticationViewModel,
                isFromProfileScreen = isFromProfileScreen
            )
        }

        composable(AuthenticationDestinations.Register.name) {

        }
    }
}
