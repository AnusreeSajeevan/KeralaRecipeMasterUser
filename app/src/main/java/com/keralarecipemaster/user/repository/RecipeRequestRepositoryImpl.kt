package com.keralarecipemaster.user.repository

import com.keralarecipemaster.user.di.CoroutinesDispatchersModule
import com.keralarecipemaster.user.domain.db.RecipeRequestsDao
import com.keralarecipemaster.user.domain.model.RecipeEntity
import com.keralarecipemaster.user.domain.model.RecipeRequestEntity
import com.keralarecipemaster.user.network.model.recipe.AddOrUpdateRecipeRequest
import com.keralarecipemaster.user.network.model.recipe.RecipeResponse
import com.keralarecipemaster.user.network.model.reciperequest.CommonRequest
import com.keralarecipemaster.user.network.model.reciperequest.RecipeRequestDtoMapper
import com.keralarecipemaster.user.network.model.reciperequest.RecipeRequestResponse
import com.keralarecipemaster.user.network.service.RecipeRequestApi
import com.keralarecipemaster.user.utils.Constants
import com.keralarecipemaster.user.utils.Diet
import com.keralarecipemaster.user.utils.Meal
import com.keralarecipemaster.user.utils.UserType
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

    override suspend fun getApprovedRecipeRequests(): Flow<List<RecipeRequestEntity>> {
        return recipeRequestsDao.getApprovedRecipeRequests()
    }

    override suspend fun getPendingRecipeRequests(): Flow<List<RecipeRequestEntity>> {
        return recipeRequestsDao.getPendingRecipeRequests()
    }

    override suspend fun addRecipeRequest(userId: Int, recipe: RecipeResponse): Flow<Pair<Int, Int>> {
        var recipeID = Constants.INVALID_RECIPE_ID
        val result = recipeRequestApi.addRecipeRequest(
            AddOrUpdateRecipeRequest(userId = userId, recipe = recipe)
        )
        if (result.isSuccessful) {
            result.body()?.let {
                recipeID = it.recipe_id
                recipeRequestsDao.insertRecipe(
                    RecipeRequestEntity(
                        recipeId = recipeID,
                        recipeName = recipe.recipeName,
                        description = recipe.description,
                        preparationMethod = recipe.preparationMethod,
                        ingredients = recipe.ingredients,
                        mealType = Meal.valueOf(recipe.mealType),
                        diet = Diet.valueOf(recipe.diet),
                        addedBy = UserType.OWNER.value,
                        rating = recipe.rating, status = "ApprovalPending",
                        restaurantAddress = recipe.resturant?.address ?: Constants.EMPTY_STRING,
                        restaurantName = recipe.resturant?.name ?: Constants.EMPTY_STRING,
                        restaurantLongitude = recipe.resturant?.longitude ?: Constants.EMPTY_STRING,
                        restaurantLatitude = recipe.resturant?.latitude ?: Constants.EMPTY_STRING
                    )
                )
            }
        }
        return flow { emit(Pair(result.code(), recipeID)) }
    }

    override suspend fun deleteRecipeRequest(recipeId: Int): Flow<Int> {
        val result = recipeRequestApi.deleteRecipe(recipeId = recipeId)
        if (result.isSuccessful) {
            withContext(Dispatchers.IO) {
                recipeRequestsDao.deleteRecipe(recipeId = recipeId)
            }
        }
        return flow { result.code() }
    }
}
