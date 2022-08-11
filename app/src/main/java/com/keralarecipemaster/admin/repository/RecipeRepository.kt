package com.keralarecipemaster.admin.repository

import androidx.lifecycle.LiveData
import com.keralarecipemaster.admin.domain.model.Recipe

interface RecipeRepository {
    val getDefaultRecipes: LiveData<List<Recipe>>
    val getUserAddedRecipes: LiveData<List<Recipe>>
    suspend fun addRecipe(recipe: Recipe)
    suspend fun count(): Int
    suspend fun deleteRecipe(recipe: Recipe)
}
