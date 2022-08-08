package com.keralarecipemaster.admin.network

import com.keralarecipemaster.admin.network.model.RecipeDto
import retrofit2.http.GET
import retrofit2.http.Header

interface RecipeService {
    @GET("/get")
    suspend fun fetchRecipes(
        @Header("Authorization") token: String
    ): List<RecipeDto>
}
