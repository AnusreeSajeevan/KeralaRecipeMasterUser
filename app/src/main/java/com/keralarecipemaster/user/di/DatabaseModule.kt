package com.keralarecipemaster.user.di

import android.content.Context
import com.keralarecipemaster.user.domain.db.FamousLocationDao
import com.keralarecipemaster.user.domain.db.RecipeDao
import com.keralarecipemaster.user.domain.db.KeralaRecipeMasterDatabase
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
    fun provideRecipeDao(keralaRecipeMasterDatabase: KeralaRecipeMasterDatabase): RecipeDao {
        return keralaRecipeMasterDatabase.getRecipeDao()
    }

    @Provides
    fun provideRecipeRequestDao(keralaRecipeMasterDatabase: KeralaRecipeMasterDatabase): RecipeRequestsDao {
        return keralaRecipeMasterDatabase.getRecipeRequestDao()
    }

    @Provides
    fun provideFamousLocationDao(keralaRecipeMasterDatabase: KeralaRecipeMasterDatabase): FamousLocationDao {
        return keralaRecipeMasterDatabase.getFamousLocationDao()
    }

    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context,
        coroutineScope: CoroutineScope
    ): KeralaRecipeMasterDatabase {
        return KeralaRecipeMasterDatabase.getDatabase(context, coroutineScope)
    }

    @Provides
    fun provideCoroutineScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }
}
