package com.keralarecipemaster.admin.di

import com.keralarecipemaster.admin.repository.RecipeRepository
import com.keralarecipemaster.admin.repository.RecipeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
// @InstallIn(ActivityComponent::class)
abstract class RecipeRepositoryModule {
    @Binds
    abstract fun bindRecipeRepositoryModule(recipeRepositoryImpl: RecipeRepositoryImpl): RecipeRepository
}
