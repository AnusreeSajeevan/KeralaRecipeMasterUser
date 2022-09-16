package com.keralarecipemaster.user.presentation.ui.reciperequests

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import com.keralarecipemaster.user.prefsstore.AuthenticationState
import com.keralarecipemaster.user.presentation.viewmodel.AuthenticationViewModel
import com.keralarecipemaster.user.presentation.viewmodel.RecipeRequestViewModel
import com.keralarecipemaster.user.utils.UserType

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecipeRequestsScreen(
    recipeRequestViewModel: RecipeRequestViewModel,
    authenticationViewModel: AuthenticationViewModel,
    navController: NavController
) {
    val recipeRequests = recipeRequestViewModel.recipeRequests

    val lifecycleOwner = LocalLifecycleOwner.current
    val recipesRequestFlowLifecycleAware = remember(recipeRequests, lifecycleOwner) {
        recipeRequests.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val recipeRequestList by recipesRequestFlowLifecycleAware.collectAsState(emptyList())

    val scaffoldState = rememberScaffoldState(
        rememberDrawerState(DrawerValue.Open)
    )

    val lifeCycleOwner = LocalLifecycleOwner.current

    val authenticationStateValue = authenticationViewModel.authenticationState
    val authenticationStateLifeCycleAware = remember(authenticationStateValue, lifeCycleOwner) {
        authenticationStateValue.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )
    }
    val authenticationState by authenticationStateLifeCycleAware.collectAsState(initial = AuthenticationState.INITIAL_STATE)

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {

                },
                modifier = Modifier.padding(bottom = 100.dp)
            ) {
                Icon(Icons.Filled.Add, "")
            }
        },
        content = {
            if (recipeRequestList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    Text(text = "No Recipes Requests", modifier = Modifier.align(Alignment.Center))
                }
            } else {
                LazyColumn(modifier = Modifier.padding(8.dp)) {
                    items(recipeRequestList) { recipeRequest ->
                        RecipeRequestComponent(
                            recipeRequest = recipeRequest,
                            recipeRequestViewModel = recipeRequestViewModel,
                            navController = navController
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                    }
                }
            }

})
}
