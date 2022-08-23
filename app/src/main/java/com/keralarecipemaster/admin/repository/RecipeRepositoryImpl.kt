package com.keralarecipemaster.admin.repository

import com.keralarecipemaster.admin.di.CoroutinesDispatchersModule
import com.keralarecipemaster.admin.domain.db.RecipeDao
import com.keralarecipemaster.admin.domain.model.Ingredient
import com.keralarecipemaster.admin.domain.model.RecipeEntity
import com.keralarecipemaster.admin.domain.model.RecipeResponseWrapper
import com.keralarecipemaster.admin.network.RecipeApi
import com.keralarecipemaster.admin.network.model.RecipeDtoMapper
import com.keralarecipemaster.admin.utils.Diet
import com.keralarecipemaster.admin.utils.Meal
import com.keralarecipemaster.admin.utils.UserType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val recipeDao: RecipeDao,
    private val recipeDtoMapper: RecipeDtoMapper,
    @CoroutinesDispatchersModule.IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val recipeApi: RecipeApi
) :
    RecipeRepository {
    override suspend fun fetchAllRecipes() {
        withContext(ioDispatcher) {
            try {
                val recipes: RecipeResponseWrapper = recipeApi.fetchRecipes()
                recipeDtoMapper.toRecipeEntityList(recipes.defaultRecipes).forEach {
                    recipeDao.insertRecipe(recipe = it)
                }
            } catch (exception: Exception) {
            }
        }
    }

    override suspend fun getDefaultRecipes(): Flow<List<RecipeEntity>> {
        return recipeDao.getAllDefaultRecipes()
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

    override suspend fun deleteRecipe(recipe: RecipeEntity) {
        withContext(Dispatchers.IO) {
            recipeDao.deleteRecipe(recipe = recipe)
        }
    }

    override suspend fun getRecipeDetails(recipeId: Int): Flow<RecipeEntity> {
        return withContext(Dispatchers.IO) {
            recipeDao.getRecipeDetails(recipeId = recipeId)
        }
    }

    override suspend fun addRecipe(recipe: RecipeEntity) {
        recipeDao.insertRecipe(recipe = recipe)
    }

    override suspend fun updateRecipe(
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
                preparationMethod = preparationMethod
            )
        }
    }
}
