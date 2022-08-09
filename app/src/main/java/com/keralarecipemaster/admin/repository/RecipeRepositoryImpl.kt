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
    override val getAllRecipes: LiveData<List<Recipe>>
        get() = recipeDao.getAllRecipes()

    override suspend fun getDefaultRecipes(): LiveData<List<Recipe>> {
        return withContext(Dispatchers.IO) {
            recipeDao.getAllRecipes()
        } /*recipeDtoMapper.toRecipeList(
            recipeService.fetchRecipes(
                token = "token"
            )
        )*/
    }

    override suspend fun count(): Int {
        return withContext(Dispatchers.IO) {
            recipeDao.numberOfRecipes()
        }
    }

    override suspend fun addRecipe(recipe: Recipe) {
        recipeDao.insertRecipe(recipe)
    }
}
