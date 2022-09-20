package com.keralarecipemaster.user.di

import android.content.Context
import com.keralarecipemaster.user.domain.db.FamousLocationDao
import com.keralarecipemaster.user.domain.db.RecipeDao
import com.keralarecipemaster.user.domain.db.RecipeMemoriesDatabase
import com.keralarecipemaster.user.domain.db.RecipeRequestsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    fun provideRecipeDao(recipeMemoriesDatabase: RecipeMemoriesDatabase): RecipeDao {
        return recipeMemoriesDatabase.getRecipeDao()
    }

    @Provides
    fun provideRecipeRequestDao(recipeMemoriesDatabase: RecipeMemoriesDatabase): RecipeRequestsDao {
        return recipeMemoriesDatabase.getRecipeRequestDao()
    }

    @Provides
    fun provideFamousLocationDao(recipeMemoriesDatabase: RecipeMemoriesDatabase): FamousLocationDao {
        return recipeMemoriesDatabase.getFamousLocationDao()
    }

    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context,
        coroutineScope: CoroutineScope
    ): RecipeMemoriesDatabase {
        return RecipeMemoriesDatabase.getDatabase(context, coroutineScope)
    }

    @Provides
    fun provideCoroutineScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }
}
