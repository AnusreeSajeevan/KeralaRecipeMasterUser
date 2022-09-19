package com.keralarecipemaster.user.repository

import com.keralarecipemaster.user.di.CoroutinesDispatchersModule
import com.keralarecipemaster.user.domain.db.RecipeRequestsDao
import com.keralarecipemaster.user.domain.model.RecipeRequestEntity
import com.keralarecipemaster.user.domain.model.RecipeRequestResponseWrapper
import com.keralarecipemaster.user.network.model.reciperequest.RecipeRequestDtoMapper
import com.keralarecipemaster.user.network.service.RecipeApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecipeRequestRepositoryImpl @Inject constructor(
    private val recipeRequestsDao: RecipeRequestsDao,
    private val recipeRequestDtoMapper: RecipeRequestDtoMapper,
    @CoroutinesDispatchersModule.IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val recipeApi: RecipeApi
) :
    RecipeRequestRepository {
    override suspend fun fetchAllRecipeRequests() {
        withContext(ioDispatcher) {
            try {
                val recipeRequests: RecipeRequestResponseWrapper = recipeApi.fetchRecipeRequests()
                recipeRequestDtoMapper.toRecipeRequestEntityList(recipeRequests.recipeRequests).forEach {
                    recipeRequestsDao.insertRecipeRequest(recipeRequest = it)
                }
            } catch (exception: Exception) {
            }
        }
       /* withContext(ioDispatcher) {
            val results = try {
                recipeApi
                    .fetchRecipeRequests().recipeRequests
            } catch (ex: Throwable) {
                println(ex)
                emptyList()
            }
            try {
                recipeRequestDtoMapper.toRecipeRequestEntityList(results).forEach {
                    recipeRequestsDao.insertRecipeRequest(recipeRequest = it)
                }
            } catch (e: Throwable) {
            }
        }*/
    }

    override suspend fun getRecipeRequestDetails(requestId: Int): Flow<RecipeRequestEntity> {
        return recipeRequestsDao.getRecipeRequestDetails(requestId = requestId)
    }

    override suspend fun getAllRecipeRequests(): Flow<List<RecipeRequestEntity>> {
        return recipeRequestsDao.getAllRecipeRequests()
    }

    override suspend fun addRecipeRequest(recipeRequestEntity: RecipeRequestEntity) {
        recipeRequestsDao.insertRecipe(recipeRequest = recipeRequestEntity)
    }
}
