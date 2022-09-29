package com.keralarecipemaster.user.network.service

import com.keralarecipemaster.user.domain.model.RecipeEntity
import com.keralarecipemaster.user.domain.model.RecipeRequestEntity
import com.keralarecipemaster.user.domain.model.RecipeRequestResponseWrapper
import com.keralarecipemaster.user.network.model.authentication.CommonResponse
import retrofit2.Response
import retrofit2.http.*

interface RecipeRequestApi {
    @GET("/get-recipe-list")
//    @HTTTP(method = "GET", path = "/get-recipe-list", hasBody = true)
    suspend fun fetchAllMyRequests(@Query("user_id") userId: Int): Response<RecipeRequestResponseWrapper>

    // owner and user
    @POST("/add-recipe")
    suspend fun addRecipeRequest(@Body recipeRequestEntity: RecipeRequestEntity): Response<CommonResponse>
}
