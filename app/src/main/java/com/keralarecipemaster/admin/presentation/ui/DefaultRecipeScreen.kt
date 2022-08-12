package com.keralarecipemaster.admin.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.keralarecipemaster.admin.domain.model.Recipe
import com.keralarecipemaster.admin.presentation.ui.view.RecipeComponent
import com.keralarecipemaster.admin.presentation.viewmodel.RecipeListViewModel
import kotlinx.coroutines.launch

@Composable
fun DefaultRecipesScreen(
    onFabClick: () -> Unit,
    recipeViewModel: RecipeListViewModel
) {
    val defaultRecipes = recipeViewModel.defaultRecipes
    val lifecycleOwner = LocalLifecycleOwner.current
    val defaultRecipesFlowLifecycleAware = remember(defaultRecipes, lifecycleOwner) {
        defaultRecipes.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val defaultRecipesList by defaultRecipesFlowLifecycleAware.collectAsState(emptyList())

    var query by remember {
        mutableStateOf("")
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onFabClick) {
                Icon(Icons.Filled.Add, "")
            }
        }
    ) {
        val coroutineScope = rememberCoroutineScope()
        Column(modifier = Modifier.padding(8.dp)) {
            OutlinedTextField(
                value = query,
                label = {
                    Text(text = "Search recipe")
                },
                onValueChange = {
                    query = it
//                    recipeViewModel.onQueryChanged(query)
                    coroutineScope.launch {
                        recipeViewModel.query.emit(it)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Filled.Search, null)
                }
            )

            Spacer(modifier = Modifier.size(10.dp))

            LazyColumn {
                items(defaultRecipesList) { recipe ->
                    RecipeComponent(
                        recipe,
                        recipeViewModel
                    )
                    Spacer(modifier = Modifier.size(10.dp))
                }
            }
        }
    }
}
