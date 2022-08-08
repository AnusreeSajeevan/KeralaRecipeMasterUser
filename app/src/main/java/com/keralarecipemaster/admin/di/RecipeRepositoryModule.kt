package com.keralarecipemaster.admin.di

import com.keralarecipemaster.admin.network.RecipeService
import com.keralarecipemaster.admin.network.model.RecipeDtoMapper
import com.keralarecipemaster.admin.network.repository.RecipeRepository
import com.keralarecipemaster.admin.network.repository.RecipeRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// @InstallIn(ViewModelComponent::class)
@InstallIn(SingletonComponent::class)
@Module
object RecipeRepositoryModule {
    //    @Binds
    @Provides
    @Singleton
    fun provideRecipeRepository(
        recipeService: RecipeService,
        recipeDtoMapper: RecipeDtoMapper
    ): RecipeRepository {
        return RecipeRepositoryImpl(
            recipeService,
            recipeDtoMapper
        )
    }
}
