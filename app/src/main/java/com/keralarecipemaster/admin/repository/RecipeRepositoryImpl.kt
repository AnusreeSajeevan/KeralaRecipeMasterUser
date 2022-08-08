package com.keralarecipemaster.admin.repository

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
    override suspend fun getDefaultRecipes(): List<Recipe> {
        return withContext(Dispatchers.IO) {
            recipeDao.getAllRecipes()
        } /*recipeDtoMapper.toRecipeList(
            recipeService.fetchRecipes(
                token = "token"
            )
        )*/
    }
}
