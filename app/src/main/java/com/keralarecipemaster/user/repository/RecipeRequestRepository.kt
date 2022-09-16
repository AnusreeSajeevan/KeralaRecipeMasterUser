package com.keralarecipemaster.user.repository

import com.keralarecipemaster.user.domain.model.RecipeRequestEntity
import kotlinx.coroutines.flow.Flow

interface RecipeRequestRepository {
    suspend fun fetchAllRecipeRequests()
    suspend fun getRecipeRequestDetails(requestId: Int): Flow<RecipeRequestEntity>
    suspend fun getAllRecipeRequests(): Flow<List<RecipeRequestEntity>>
}
