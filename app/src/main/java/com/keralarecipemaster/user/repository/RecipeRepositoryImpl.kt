package com.keralarecipemaster.user.repository

import com.keralarecipemaster.user.di.CoroutinesDispatchersModule
import com.keralarecipemaster.user.domain.db.RecipeDao
import com.keralarecipemaster.user.domain.model.Ingredient
import com.keralarecipemaster.user.domain.model.RecipeEntity
import com.keralarecipemaster.user.domain.model.RecipeResponseWrapper
import com.keralarecipemaster.user.network.service.RecipeApi
import com.keralarecipemaster.user.network.model.recipe.RecipeDtoMapper
import com.keralarecipemaster.user.utils.Diet
import com.keralarecipemaster.user.utils.Meal
import com.keralarecipemaster.user.utils.UserType
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
                recipeDtoMapper.toRecipeEntityList(recipes.recipes).forEach {
                    recipeDao.insertRecipe(recipe = it)
                }
            } catch (exception: Exception) {
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
