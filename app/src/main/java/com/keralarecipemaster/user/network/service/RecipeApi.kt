package com.keralarecipemaster.user.network.service

import com.keralarecipemaster.user.domain.model.RecipeEntity
import com.keralarecipemaster.user.domain.model.RecipeRequestResponseWrapper
import com.keralarecipemaster.user.domain.model.RecipeResponseWrapper
import com.keralarecipemaster.user.network.model.authentication.CommonResponse
import com.keralarecipemaster.user.network.model.recipe.RecipeResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RecipeApi {
    @GET("/recipes")
    suspend fun fetchRecipes(): RecipeResponseWrapper

    @GET("/recipe-requests")
    suspend fun fetchRecipeRequests(): RecipeRequestResponseWrapper

    @POST("/add-recipe")
    suspend fun addRecipe(@Body recipeEntity: RecipeEntity): retrofit2.Response<CommonResponse>
}