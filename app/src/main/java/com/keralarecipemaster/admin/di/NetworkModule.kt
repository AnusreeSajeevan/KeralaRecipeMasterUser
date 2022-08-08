package com.keralarecipemaster.admin.di

import com.google.gson.GsonBuilder
import com.keralarecipemaster.admin.network.RecipeService
import com.keralarecipemaster.admin.network.model.RecipeDtoMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private val BASE_URL = "https://food2fork.ca/api/recipe/"

    @Singleton
    @Provides
    fun providesRecipeMapper(): RecipeDtoMapper {
        return RecipeDtoMapper()
    }

    @Singleton
    @Provides
    fun providesRecipeService(): RecipeService {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create())).build()
            .create(RecipeService::class.java)
    }
}
