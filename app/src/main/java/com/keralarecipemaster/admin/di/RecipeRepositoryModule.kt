package com.keralarecipemaster.admin.di

import com.keralarecipemaster.admin.domain.db.RecipeDao
import com.keralarecipemaster.admin.network.RecipeService
import com.keralarecipemaster.admin.network.model.RecipeDtoMapper
import com.keralarecipemaster.admin.repository.RecipeRepository
import com.keralarecipemaster.admin.repository.RecipeRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
        recipeDao: RecipeDao,
        recipeService: RecipeService,
        recipeDtoMapper: RecipeDtoMapper
    ): RecipeRepository {
        return RecipeRepositoryImpl(
            recipeDao,
            recipeService,
            recipeDtoMapper
        )
    }
}
