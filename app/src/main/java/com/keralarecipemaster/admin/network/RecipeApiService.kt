package com.keralarecipemaster.admin.network

import com.keralarecipemaster.admin.model.Recipe
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private val BASE_URL = "https://urlgoeshere.com"
val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface RecipeApiService {
    @GET("recipes")
    suspend fun getRecipes(): List<Recipe>
}

object RecipeApi {
    val recipeApiService: RecipeApiService by lazy {
        retrofit.create(RecipeApiService::class.java)
    }
}
