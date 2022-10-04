package com.keralarecipemaster.user.repository

import android.util.Log
import com.keralarecipemaster.user.di.CoroutinesDispatchersModule
import com.keralarecipemaster.user.domain.db.FamousLocationDao
import com.keralarecipemaster.user.domain.db.RecipeDao
import com.keralarecipemaster.user.domain.model.FamousRestaurant
import com.keralarecipemaster.user.domain.model.RecipeEntity
import com.keralarecipemaster.user.network.model.recipe.AddOrUpdateRecipeRequest
import com.keralarecipemaster.user.network.model.recipe.RecipeDtoMapper
import com.keralarecipemaster.user.network.model.recipe.RecipeResponse
import com.keralarecipemaster.user.network.service.RecipeApi
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

class RecipeRepositoryImpl @Inject constructor(
    private val recipeDao: RecipeDao,
    private val famousLocationDao: FamousLocationDao,
    private val recipeDtoMapper: RecipeDtoMapper,
    @CoroutinesDispatchersModule.IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val recipeApi: RecipeApi
) :
    RecipeRepository {
    override suspend fun fetchFamousRecipes() {
        withContext(ioDispatcher) {
            val results = recipeApi.fetchFamousRecipes()
            Log.d("RecipeListResponse", results.message())
            Log.d("RecipeListResponse", results.body().toString())
            if (results.isSuccessful) {
                recipeDao.deleteAllFamousRecipes()
                val list = results.body()?.recipes ?: emptyList()
                if (list.isNotEmpty()) {
                    recipeDtoMapper.toRecipeEntityList(list).forEach {
                        recipeDao.insertRecipe(recipe = it)
                        famousLocationDao.insertFamousLocation(
                            FamousRestaurant(
                                System.currentTimeMillis() / 1000,
                                it.restaurantName,
                                it.restaurantAddress,
                                it.restaurantLatitude,
                                it.restaurantLongitude
                            )
                        )
                    }
                }
            }
        }
    }

    override suspend fun fetchMyRecipes(userId: Int) {
        withContext(ioDispatcher) {
            val results = recipeApi.fetchMyRecipes(userId)
            Log.d("MyRecipeListResponse", results.message())
            Log.d("MyRecipeListResponse", results.body().toString())
            if (results.isSuccessful) {
                recipeDao.deleteAllMyRecipes()
                val list = results.body()?.recipes ?: emptyList()
                if (list.isNotEmpty()) {
                    recipeDtoMapper.toRecipeEntityList(list).forEach {
                        recipeDao.insertRecipe(recipe = it)
                    }
                }
            }
        }
    }

    override suspend fun getFamousRecipes(): Flow<List<RecipeEntity>> {
        return recipeDao.getAllFamousRecipes()
    }

    override suspend fun getUserAddedRecipes(): Flow<List<RecipeEntity>> {
        return recipeDao.getAllUserAddedRecipes()
    }

    override fun searchResults(query: String, addedBy: UserType): Flow<List<RecipeEntity>> {
        return recipeDao.search(queryString = query, addedBy = addedBy.name)
    }

    override suspend fun count(): Int {
        return withContext(Dispatchers.IO) {
            recipeDao.numberOfRecipes()
        }
    }

    override suspend fun getRecipeDetails(recipeId: Int): Flow<RecipeEntity> {
        return withContext(Dispatchers.IO) {
            recipeDao.getRecipeDetails(recipeId = recipeId)
        }
    }

    override suspend fun insertRecipe(recipe: RecipeEntity) {
        recipeDao.insertRecipe(recipe)
    }

    override suspend fun addRecipe(userId: Int, recipe: RecipeResponse): Flow<Pair<Int, Int>> {
        var recipeID = Constants.INVALID_RECIPE_ID
        val result = recipeApi.addRecipe(
            AddOrUpdateRecipeRequest(userId = userId, recipe = recipe)
        )
        if (result.isSuccessful) {
            result.body()?.let {
                recipeID = it.recipe_id
                recipeDao.insertRecipe(
                    RecipeEntity(
                        id = recipeID,
                        recipeName = recipe.recipeName,
                        description = recipe.description,
                        preparationMethod = recipe.preparationMethod,
                        ingredients = recipe.ingredients,
                        mealType = Meal.valueOf(recipe.mealType),
                        image = recipe.image,
                        imageName = recipe.imageName,
                        diet = Diet.valueOf(recipe.diet),
                        addedBy = UserType.USER,
                        rating = recipe.rating, status = "",
                        restaurantAddress = Constants.EMPTY_STRING,
                        restaurantName = Constants.EMPTY_STRING,
                        restaurantLongitude = Constants.EMPTY_STRING,
                        restaurantLatitude = Constants.EMPTY_STRING
                    )
                )
            }
        }
        return flow { emit(Pair(result.code(), recipeID)) }
    }

    override suspend fun updateRecipe(userId: Int, recipe: RecipeResponse): Flow<Int> {
        val result =
            recipeApi.updateRecipe(AddOrUpdateRecipeRequest(userId = userId, recipe = recipe))
        if (result.isSuccessful) {
            result.body()?.let {
                withContext(Dispatchers.IO) {
                    recipeDao.updateRecipe(
                        id = recipe.id,
                        recipeName = recipe.recipeName,
                        description = recipe.description,
                        preparationMethod = recipe.preparationMethod,
                        ingredients = recipe.ingredients,
                        meal = Meal.valueOf(recipe.mealType),
                        diet = Diet.valueOf(recipe.diet),
                        rating = recipe.rating
                    )
                }
            }
        }
        return flow { emit(result.code()) }
    }

/*    override suspend fun updateRecipe(
        recipeId: Int,
        recipeName: String,
        description: String,
        diet: Diet,
        meal: Meal,
        ingredients: List<Ingredient>,
        preparationMethod: String
    ) {
        withContext(Dispatchers.IO) {
            recipeDao.updateRecipe(
                recipeName = recipeName,
                id = recipeId,
                description = description,
                diet = diet,
                meal = meal,
                ingredients = ingredients,
                preparationMethod = preparationMethod,
                rating =
            )
        }
    }*/

    override suspend fun deleteRecipe(recipeId: Int): Flow<Int> {
        val result = recipeApi.deleteRecipe(recipeId = recipeId)
        if (result.isSuccessful) {
            withContext(Dispatchers.IO) {
                recipeDao.deleteRecipe(recipeId = recipeId)
            }
        }
        return flow { result.code() }
    }
}
