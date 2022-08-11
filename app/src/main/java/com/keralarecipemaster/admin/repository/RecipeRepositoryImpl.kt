package com.keralarecipemaster.admin.repository

import androidx.lifecycle.LiveData
import com.keralarecipemaster.admin.domain.db.RecipeDao
import com.keralarecipemaster.admin.domain.model.Recipe
import com.keralarecipemaster.admin.network.RecipeService
import com.keralarecipemaster.admin.network.model.RecipeDtoMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipeRepositoryImpl(
    private val recipeDao: RecipeDao,
    private val recipeService: RecipeService,
    private val recipeDtoMapper: RecipeDtoMapper
) :
    RecipeRepository {
    override val getDefaultRecipes: LiveData<List<Recipe>>
        get() = recipeDao.getAllDefaultRecipes()
    override val getUserAddedRecipes: LiveData<List<Recipe>>
        get() = recipeDao.getAllUserAddedRecipes()

    override suspend fun count(): Int {
        return withContext(Dispatchers.IO) {
            recipeDao.numberOfRecipes()
        }
    }

    override suspend fun deleteRecipe(recipe: Recipe) {
        withContext(Dispatchers.IO) {
            recipeDao.deleteRecipe(recipe)
        }
    }

    override suspend fun addRecipe(recipe: Recipe) {
        recipeDao.insertRecipe(recipe)
    }
}
