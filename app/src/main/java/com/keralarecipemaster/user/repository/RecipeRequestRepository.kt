package com.keralarecipemaster.user.repository

import com.keralarecipemaster.user.domain.model.RecipeEntity
import com.keralarecipemaster.user.domain.model.RecipeRequestEntity
import kotlinx.coroutines.flow.Flow

interface RecipeRequestRepository {
    suspend fun fetchAllMyRecipeRequests(userId: Int)
    suspend fun getRecipeRequestDetails(requestId: Int): Flow<RecipeRequestEntity>
    suspend fun getAllRecipeRequests(): Flow<List<RecipeRequestEntity>>
    suspend fun addRecipeRequest(recipeRequestEntity: RecipeRequestEntity): Flow<Int>
    suspend fun deleteRecipeRequest(recipeRequestEntity: RecipeRequestEntity)
}
