package com.keralarecipemaster.user.di

import com.keralarecipemaster.user.repository.RecipeRequestRepository
import com.keralarecipemaster.user.repository.RecipeRequestRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RecipeRequestRepositoryModule {
    @Binds
    abstract fun provideRecipeRequestRepository(repository: RecipeRequestRepositoryImpl): RecipeRequestRepository
}
