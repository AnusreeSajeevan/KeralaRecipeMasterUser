package com.keralarecipemaster.admin.repository

import com.keralarecipemaster.admin.domain.model.Ingredient
import com.keralarecipemaster.admin.domain.model.RecipeEntity
import com.keralarecipemaster.admin.utils.Diet
import com.keralarecipemaster.admin.utils.Meal
import com.keralarecipemaster.admin.utils.UserType
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    suspend fun fetchAllRecipes()
    suspend fun getDefaultRecipes(): Flow<List<RecipeEntity>>
    suspend fun getUserAddedRecipes(): Flow<List<RecipeEntity>>
    fun searchResults(querString: String, addedBy: UserType): Flow<List<RecipeEntity>>
    suspend fun addRecipe(recipe: RecipeEntity)
    suspend fun updateRecipe(recipeId: Int, recipeName: String, description: String, diet: Diet, meal: Meal, ingredients: List<Ingredient>, preparationMethod: String)
    suspend fun count(): Int
    suspend fun deleteRecipe(recipe: RecipeEntity)
    suspend fun getRecipeDetails(recipeId: Int): Flow<RecipeEntity>
}
