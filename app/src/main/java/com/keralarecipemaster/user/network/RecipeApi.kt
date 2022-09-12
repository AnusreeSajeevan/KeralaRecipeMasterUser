package com.keralarecipemaster.user.network

import com.keralarecipemaster.user.domain.model.RecipeResponseWrapper
import retrofit2.http.GET

interface RecipeApi {
    @GET("/recipes")
    suspend fun fetchRecipes(): RecipeResponseWrapper
}