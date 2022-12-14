package com.keralarecipemaster.user.di

import com.keralarecipemaster.user.repository.RecipeRepository
import com.keralarecipemaster.user.repository.RecipeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RecipeRepositoryModule {
    @Binds
    abstract fun provideRecipeRepository(repository: RecipeRepositoryImpl): RecipeRepository
}
