package com.keralarecipemaster.admin.presentation.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.keralarecipemaster.admin.domain.model.Recipe
import com.keralarecipemaster.admin.presentation.ui.view.RecipeComponent

@Composable
fun UserAddedRecipesScreen(userAddedRecipes: List<Recipe>) {
    LazyColumn {
        items(userAddedRecipes) { recipe ->
            RecipeComponent(
                recipe
            )
        }
    }
}
