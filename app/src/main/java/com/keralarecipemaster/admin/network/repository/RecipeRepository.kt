package com.keralarecipemaster.admin.network.repository

import com.keralarecipemaster.admin.domain.model.Recipe

interface RecipeRepository {
    suspend fun getDefaultRecipes(token: String): List<Recipe>
}
