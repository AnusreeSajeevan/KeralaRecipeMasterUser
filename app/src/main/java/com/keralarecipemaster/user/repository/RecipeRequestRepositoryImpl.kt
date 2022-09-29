package com.keralarecipemaster.user.repository

import com.keralarecipemaster.user.di.CoroutinesDispatchersModule
import com.keralarecipemaster.user.domain.db.RecipeRequestsDao
import com.keralarecipemaster.user.domain.model.RecipeEntity
import com.keralarecipemaster.user.domain.model.RecipeRequestEntity
import com.keralarecipemaster.user.network.model.reciperequest.CommonRequest
import com.keralarecipemaster.user.network.model.reciperequest.RecipeRequestDtoMapper
import com.keralarecipemaster.user.network.service.RecipeRequestApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecipeRequestRepositoryImpl @Inject constructor(
    private val recipeRequestsDao: RecipeRequestsDao,
    private val recipeRequestDtoMapper: RecipeRequestDtoMapper,
    @CoroutinesDispatchersModule.IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val recipeRequestApi: RecipeRequestApi
) :
    RecipeRequestRepository {
    override suspend fun fetchAllMyRecipeRequests(userId: Int) {
        withContext(ioDispatcher) {
            val result = recipeRequestApi.fetchAllMyRequests(userId)
            if (result.isSuccessful) {
                val requests = result.body()?.recipeRequests ?: emptyList()
                recipeRequestsDao.deleteAll()
                if (requests.isNotEmpty()) {
                    recipeRequestDtoMapper.toRecipeRequestEntityList(requests).forEach {
                        recipeRequestsDao.insertRecipeRequest(recipeRequest = it)
                    }
                }
            }
        }
    }

    override suspend fun getRecipeRequestDetails(requestId: Int): Flow<RecipeRequestEntity> {
        return recipeRequestsDao.getRecipeRequestDetails(requestId = requestId)
    }

    override suspend fun getAllRecipeRequests(): Flow<List<RecipeRequestEntity>> {
        return recipeRequestsDao.getAllRecipeRequests()
    }

    override suspend fun addRecipeRequest(recipeRequestEntity: RecipeRequestEntity):Flow<Int> {
        val result = recipeRequestApi.addRecipeRequest(recipeRequestEntity = recipeRequestEntity)
        if (result.isSuccessful) {
            recipeRequestsDao.insertRecipe(recipeRequest = recipeRequestEntity)
        }
        return flow { result.code() }
    }

    override suspend fun deleteRecipeRequest(recipeRequestEntity: RecipeRequestEntity) {
        withContext(Dispatchers.IO) {
            recipeRequestsDao.deleteRecipe(recipeRequestEntity = recipeRequestEntity)
        }
    }
}
