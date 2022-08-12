package com.keralarecipemaster.admin.repository

import androidx.lifecycle.LiveData
import com.keralarecipemaster.admin.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    fun fetchAllRecipes()
    suspend fun getDefaultRecipes(): Flow<List<Recipe>>

    fun searchResults(querString: String): Flow<List<Recipe>>

        val getUserAddedRecipes: LiveData<List<Recipe>>
    suspend fun addRecipe(recipe: Recipe)
    suspend fun count(): Int
    suspend fun deleteRecipe(recipe: Recipe)
}
