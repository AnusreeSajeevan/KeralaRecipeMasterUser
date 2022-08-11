package com.keralarecipemaster.admin.presentation.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.keralarecipemaster.admin.domain.model.Recipe
import com.keralarecipemaster.admin.presentation.ui.view.RecipeComponent
import com.keralarecipemaster.admin.presentation.viewmodel.RecipeListViewModel
import com.keralarecipemaster.admin.utils.Diet
import com.keralarecipemaster.admin.utils.Meal
import com.keralarecipemaster.admin.utils.UserType

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
        LazyColumn {
            items(recipes) { recipe ->
                RecipeComponent(
                    recipe,
                    recipeViewModel
                )
            }
        }
    }
}