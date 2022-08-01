package com.keralarecipemaster.admin.repository

import com.keralarecipemaster.admin.model.Recipe

interface RecipeRepository {
    suspend fun getDefaultRecipes(): List<Recipe>
}
