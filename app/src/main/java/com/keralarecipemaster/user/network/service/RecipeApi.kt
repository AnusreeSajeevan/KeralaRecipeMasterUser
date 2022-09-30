package com.keralarecipemaster.user.network.service

import com.keralarecipemaster.admin.network.model.DeleteRecipeResponse
import com.keralarecipemaster.user.domain.model.RecipeResponseWrapper
import com.keralarecipemaster.user.network.model.authentication.AddRecipeResponse
import com.keralarecipemaster.user.network.model.recipe.AddOrUpdateRecipeRequest
import retrofit2.Response
import retrofit2.http.*

interface RecipeApi {

    @GET("/famous-recipe-list")
    suspend fun fetchFamousRecipes(): Response<RecipeResponseWrapper>

    @GET("/get-recipe-list/")
    suspend fun fetchMyRecipes(@Query("user_id") userId: Int): Response<RecipeResponseWrapper>

    // owner and user
    @POST("/add-recipe")
    suspend fun addRecipe(@Body addRecipeRequest: AddOrUpdateRecipeRequest): Response<AddRecipeResponse>

    @PUT("/update-recipe")
    suspend fun updateRecipe(@Body updateRecipeRequest: AddOrUpdateRecipeRequest): Response<AddRecipeResponse>

//    @HTTP(method = "DELETE", path = "/delete-recipe", hasBody = true)
    @DELETE("/delete-recipe/")
    suspend fun deleteRecipe(@Query("recipe_id") recipeId: Int): Response<DeleteRecipeResponse>
}
