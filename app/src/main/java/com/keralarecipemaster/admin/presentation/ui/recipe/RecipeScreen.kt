package com.keralarecipemaster.admin.presentation.ui.recipe

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import com.keralarecipemaster.admin.presentation.ui.view.RecipeComponent
import com.keralarecipemaster.admin.presentation.viewmodel.RecipeListViewModel
import com.keralarecipemaster.admin.utils.UserType

@Composable
fun RecipesScreen(
    onFabClick: (() -> Unit)? = null,
    recipeViewModel: RecipeListViewModel,
    userType: UserType,
    navController: NavController
) {
    val recipes =
        if (userType == UserType.ADMIN) recipeViewModel.defaultRecipes else recipeViewModel.userAddedRecipes

    val lifecycleOwner = LocalLifecycleOwner.current
    val recipesRecipesFlowLifecycleAware = remember(recipes, lifecycleOwner) {
        recipes.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val recipesList by recipesRecipesFlowLifecycleAware.collectAsState(emptyList())

    var query by remember {
        mutableStateOf("")
    }
    Scaffold(
        floatingActionButton = {
            onFabClick?.let {
                FloatingActionButton(onClick = onFabClick!!, modifier = Modifier.padding(bottom = 100.dp)) {
                    Icon(Icons.Filled.Add, "")
                }
            }
        }
    ) {
        val coroutineScope = rememberCoroutineScope()
        Column(modifier = Modifier.padding(bottom = 100.dp, top = 8.dp, start = 8.dp, end = 8.dp)) {
            OutlinedTextField(
                value = query,
                label = {
                    Text(text = "Search recipe")
                },
                onValueChange = {
                    query = it
                    recipeViewModel.onQueryChanged(query, userType)
                },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Filled.Search, null)
                }
            )

            Spacer(modifier = Modifier.size(10.dp))

            LazyColumn {
                items(recipesList) { recipe ->
                    RecipeComponent(
                        recipe,
                        recipeViewModel,
                        navController
                    )
                    Spacer(modifier = Modifier.size(10.dp))
                }
            }
        }
    }
}
