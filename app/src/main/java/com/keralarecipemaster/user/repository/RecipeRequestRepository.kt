package com.keralarecipemaster.user.repository

import com.keralarecipemaster.user.domain.model.RecipeRequestEntity
import com.keralarecipemaster.user.network.model.recipe.RecipeResponse
import kotlinx.coroutines.flow.Flow

interface RecipeRequestRepository {
    suspend fun fetchAllMyRecipeRequests(userId: Int)
    suspend fun addRecipeRequest(userId: Int, recipe: RecipeResponse): Flow<Pair<Int, Int>>
    suspend fun deleteRecipeRequest(recipeId: Int): Flow<Int>

    suspend fun getAllRecipeRequests(): Flow<List<RecipeRequestEntity>>
    suspend fun getApprovedRecipeRequests(): Flow<List<RecipeRequestEntity>>
    suspend fun getPendingRecipeRequests(): Flow<List<RecipeRequestEntity>>
    suspend fun getRecipeRequestDetails(requestId: Int): Flow<RecipeRequestEntity>
}
