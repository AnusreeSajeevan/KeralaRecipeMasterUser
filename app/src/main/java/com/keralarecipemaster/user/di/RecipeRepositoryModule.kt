package com.keralarecipemaster.user.di

import com.keralarecipemaster.user.repository.RecipeRepository
import com.keralarecipemaster.user.repository.RecipeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

// @InstallIn(ViewModelComponent::class)
@InstallIn(SingletonComponent::class)
@Module
abstract class RecipeRepositoryModule {
    @Binds
    abstract fun provideTravelRepository(repository: RecipeRepositoryImpl): RecipeRepository
    /*  //    @Binds
      @Provides
      @Singleton
       fun provideRecipeRepository(
          recipeDao: RecipeDao,
          recipeService: RecipeService,
          recipeDtoMapper: RecipeDtoMapper,
          coroutineDispatcher: CoroutineDispatcher,
          recipeApi: RecipeApi
      ): RecipeRepository {
          return RecipeRepositoryImpl(
              recipeDao,
              recipeService,
              recipeDtoMapper,
              coroutineDispatcher,
              recipeApi = recipeApi
          )
      }*/
}