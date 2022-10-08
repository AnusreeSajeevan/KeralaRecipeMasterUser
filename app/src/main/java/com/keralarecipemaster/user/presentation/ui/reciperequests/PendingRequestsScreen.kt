package com.keralarecipemaster.user.presentation.ui.reciperequests

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import com.keralarecipemaster.user.presentation.ui.recipe.add.AddRecipeActivity
import com.keralarecipemaster.user.presentation.viewmodel.RecipeRequestViewModel
import com.keralarecipemaster.user.utils.Constants

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PendingRequestsScreen(
    recipeRequestViewModel: RecipeRequestViewModel,
    navController: NavController
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    val pendingRecipeRequests = recipeRequestViewModel.pendingRecipeRequests
    val pendingRecipesRequestFlowLifecycleAware = remember(pendingRecipeRequests, lifecycleOwner) {
        pendingRecipeRequests.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val pendingRecipeRequestList by pendingRecipesRequestFlowLifecycleAware.collectAsState(emptyList())

    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current

    val shouldFetchMyRecipesValue = recipeRequestViewModel.shouldFetchMyRecipes
    val shouldFetchMyRecipesLifeCycleAware = remember(shouldFetchMyRecipesValue, lifeCycleOwner) {
        shouldFetchMyRecipesValue.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )
    }
    val shouldFetchMyRecipes by shouldFetchMyRecipesLifeCycleAware.collectAsState(initial = false)

    if (shouldFetchMyRecipes) {
        recipeRequestViewModel.fetchRecipeRequests()
        recipeRequestViewModel.resetShouldFetch()
    }

    val errorMessageValue = recipeRequestViewModel.errorMessage
    val errorMessageValueLifeCycleAware =
        remember(errorMessageValue, lifeCycleOwner) {
            errorMessageValue.flowWithLifecycle(
                lifeCycleOwner.lifecycle,
                Lifecycle.State.STARTED
            )
        }
    val errorMessage by errorMessageValueLifeCycleAware.collectAsState(initial = Constants.EMPTY_STRING)

    if (errorMessage.isNotEmpty()) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        recipeRequestViewModel.resetErrorMessage()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    context.startActivity(Intent(context, AddRecipeActivity::class.java))
                },
                modifier = Modifier.padding(bottom = 100.dp)
            ) {
                Icon(Icons.Filled.Add, "")
            }
        },
        content = {
            if (pendingRecipeRequestList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    Text(
                        text = "You haven't added any recipes for approval\nClick on the + button to add a new recipe!",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            } else {
                ConstraintLayout {
                    Column {
                        Text(
                            text = "You have pending Recipe Requests",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp)
                        )
                        LazyColumn(
                            modifier = Modifier
                                .padding(
                                    start = 8.dp,
                                    end = 8.dp,
                                    top = 8.dp,
                                    bottom = 16.dp
                                )
                                .fillMaxHeight()
                        ) {
                            items(pendingRecipeRequestList) { recipeRequest ->
                                RecipeRequestComponent(
                                    recipeRequest = recipeRequest,
                                    recipeRequestViewModel = recipeRequestViewModel,
                                    navController = navController
                                )
                                Spacer(modifier = Modifier.size(10.dp))
                            }
                        }
                    }
                }
            }
        }
    )
}
