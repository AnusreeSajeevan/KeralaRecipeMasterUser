package com.keralarecipemaster.admin.network.repository

import com.keralarecipemaster.admin.domain.model.Recipe
import com.keralarecipemaster.admin.network.RecipeService
import com.keralarecipemaster.admin.network.model.RecipeDtoMapper
import javax.inject.Inject

class RecipeRepositoryImpl(
    private val recipeService: RecipeService,
    private val recipeDtoMapper: RecipeDtoMapper
) :
    RecipeRepository {
    override suspend fun getDefaultRecipes(token: String): List<Recipe> {
        return recipeDtoMapper.toRecipeList(
            recipeService.fetchRecipes(
                token = token
            )
        )
    }
}
