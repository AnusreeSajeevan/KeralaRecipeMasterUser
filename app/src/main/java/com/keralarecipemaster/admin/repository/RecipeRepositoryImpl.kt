package com.keralarecipemaster.admin.repository

import com.keralarecipemaster.admin.model.Recipe
import com.keralarecipemaster.admin.network.RecipeApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor() :
    RecipeRepository {
    override suspend fun getDefaultRecipes(): List<Recipe> {
        return withContext(Dispatchers.Default) {
            RecipeApi.recipeApiService.getRecipes()
        }
    }
}
