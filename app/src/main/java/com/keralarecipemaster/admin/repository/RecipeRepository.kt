package com.keralarecipemaster.admin.repository

import androidx.lifecycle.LiveData
import com.keralarecipemaster.admin.domain.model.Recipe

interface RecipeRepository {
    val getAllRecipes: LiveData<List<Recipe>>
    suspend fun getDefaultRecipes(): LiveData<List<Recipe>>
    suspend fun addRecipe(recipe: Recipe)
    suspend fun count(): Int
}
