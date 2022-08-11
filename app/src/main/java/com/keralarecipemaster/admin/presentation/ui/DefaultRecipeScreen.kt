package com.keralarecipemaster.admin.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.keralarecipemaster.admin.domain.model.Recipe
import com.keralarecipemaster.admin.presentation.ui.view.RecipeComponent
import com.keralarecipemaster.admin.presentation.viewmodel.RecipeListViewModel

@Composable
fun DefaultRecipesScreen(
    recipes: List<Recipe>,
    onFabClick: () -> Unit,
    recipeViewModel: RecipeListViewModel
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onFabClick) {
                Icon(Icons.Filled.Add, "")
            }
        }
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            OutlinedTextField(
                value = "",
                label = {
                    Text(text = "Search recipe")
                },
                onValueChange = {
                    recipeViewModel.onQueryChanged()
                },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Filled.Search, null)
                }
            )

            Spacer(modifier = Modifier.size(10.dp))

            LazyColumn {
                items(recipes) { recipe ->
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
