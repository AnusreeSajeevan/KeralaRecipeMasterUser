package com.keralarecipemaster.user.repository

import com.keralarecipemaster.user.domain.model.RecipeEntity
import com.keralarecipemaster.user.network.model.recipe.RecipeResponse
import com.keralarecipemaster.user.utils.UserType
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    suspend fun fetchFamousRecipes()
    suspend fun fetchMyRecipes(userId: Int)
    suspend fun addRecipe(userId: Int, recipe: RecipeResponse): Flow<Pair<Int, Int>>
    suspend fun updateRecipe(userId: Int, recipe: RecipeResponse): Flow<Int>
    suspend fun deleteRecipe(recipeId: Int): Flow<Int>

    suspend fun getFamousRecipes(): Flow<List<RecipeEntity>>
    suspend fun getUserAddedRecipes(): Flow<List<RecipeEntity>>
    fun searchResults(querString: String, addedBy: UserType): Flow<List<RecipeEntity>>
    suspend fun count(): Int
    suspend fun getRecipeDetails(recipeId: Int): Flow<RecipeEntity>
    suspend fun insertRecipe(recipe: RecipeEntity)
}
