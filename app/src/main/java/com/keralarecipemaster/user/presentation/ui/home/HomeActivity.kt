package com.keralarecipemaster.user.presentation.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.keralarecipemaster.user.prefsstore.AuthenticationState
import com.keralarecipemaster.user.presentation.ui.recipe.RecipeApp
import com.keralarecipemaster.user.presentation.viewmodel.AuthenticationViewModel
import com.keralarecipemaster.user.presentation.viewmodel.RecipeListViewModel
import com.keralarecipemaster.user.presentation.viewmodel.RecipeRequestViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    private val recipeViewModel: RecipeListViewModel by viewModels()
    private val authenticationViewModel: AuthenticationViewModel by viewModels()
    private val recipeRequestViewModel: RecipeRequestViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val backstackEntry = navController.currentBackStackEntryAsState()
            val currentScreen = HomeItems.fromRoute(backstackEntry.value?.destination?.route)
            val scaffoldState = rememberScaffoldState()
            val lifeCycleOwner = LocalLifecycleOwner.current

            val authenticationStateValue = authenticationViewModel.authenticationState
            val authenticationStateValueLifeCycleAware =
                remember(authenticationStateValue, lifeCycleOwner) {
                    authenticationStateValue.flowWithLifecycle(
                        lifeCycleOwner.lifecycle,
                        Lifecycle.State.STARTED
                    )
                }
            val authenticationState by authenticationStateValueLifeCycleAware.collectAsState(initial = AuthenticationState.INITIAL_STATE)
            if (authenticationState != AuthenticationState.INITIAL_STATE) {
                RecipeApp(
                    recipeListViewModel = recipeViewModel,
                    authenticationViewModel = authenticationViewModel,
                    recipeRequestViewModel = recipeRequestViewModel
                )
            } /*else {
                ShowLoginScreen(

                    authenticationViewModel = authenticationViewModel
                )
            }*/
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }
}
