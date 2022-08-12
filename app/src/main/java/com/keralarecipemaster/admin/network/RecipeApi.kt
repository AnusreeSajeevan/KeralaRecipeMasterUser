package com.keralarecipemaster.admin.network

import com.keralarecipemaster.admin.domain.model.RecipeResponseWrapper
import retrofit2.http.GET

interface RecipeApi {
    @GET("/recipes")
    fun fetchRecipes(): RecipeResponseWrapper
}