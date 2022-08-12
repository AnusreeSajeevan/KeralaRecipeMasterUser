package com.keralarecipemaster.admin.presentation.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.keralarecipemaster.admin.domain.model.RecipeEntity
import com.keralarecipemaster.admin.presentation.ui.view.RecipeComponent
import com.keralarecipemaster.admin.presentation.viewmodel.RecipeListViewModel

@Composable
fun UserAddedRecipesScreen(userAddedRecipes: List<RecipeEntity>, recipeViewModel: RecipeListViewModel) {
    LazyColumn {
        items(userAddedRecipes) { recipe ->
            RecipeComponent(
                recipe, recipeViewModel
            )
        }
    }
}
