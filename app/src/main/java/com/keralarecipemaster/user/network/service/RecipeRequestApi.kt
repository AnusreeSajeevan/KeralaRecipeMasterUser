package com.keralarecipemaster.user.network.service

import com.keralarecipemaster.user.domain.model.RecipeRequestResponseWrapper
import com.keralarecipemaster.user.network.model.DeleteRecipeResponse
import com.keralarecipemaster.user.network.model.authentication.AddRecipeResponse
import com.keralarecipemaster.user.network.model.recipe.AddOrUpdateRecipeRequest
import retrofit2.Response
import retrofit2.http.*

interface RecipeRequestApi {
    @GET("/get-recipe-list/")
    suspend fun fetchAllMyRequests(@Query("user_id") userId: Int): Response<RecipeRequestResponseWrapper>

    @POST("/add-recipe")
    suspend fun addRecipeRequest(@Body addRecipeRequest: AddOrUpdateRecipeRequest): Response<AddRecipeResponse>

    @DELETE("/delete-recipe/")
    suspend fun deleteRecipe(@Query("recipe_id") recipeId: Int): Response<DeleteRecipeResponse>
}
