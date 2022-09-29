package com.keralarecipemaster.user.repository

import com.keralarecipemaster.user.domain.model.Ingredient
import com.keralarecipemaster.user.domain.model.RecipeEntity
import com.keralarecipemaster.user.network.model.recipe.RecipeResponse
import com.keralarecipemaster.user.utils.Diet
import com.keralarecipemaster.user.utils.Meal
import com.keralarecipemaster.user.utils.UserType
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    suspend fun fetchFamousRecipes()
    suspend fun fetchMyRecipes(userId: Int)
    suspend fun addRecipe(userId: Int, recipe: RecipeResponse): Flow<Pair<Int, Int>>
    suspend fun deleteRecipe(recipe: RecipeEntity)

    suspend fun getFamousRecipes(): Flow<List<RecipeEntity>>
    suspend fun getUserAddedRecipes(): Flow<List<RecipeEntity>>
    fun searchResults(querString: String, addedBy: UserType): Flow<List<RecipeEntity>>
    suspend fun updateRecipe(recipeId: Int, recipeName: String, description: String, diet: Diet, meal: Meal, ingredients: List<Ingredient>, preparationMethod: String)
    suspend fun count(): Int
    suspend fun getRecipeDetails(recipeId: Int): Flow<RecipeEntity>
    suspend fun insertRecipe(recipe: RecipeEntity)
}
