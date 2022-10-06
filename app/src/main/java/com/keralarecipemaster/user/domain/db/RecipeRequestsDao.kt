package com.keralarecipemaster.user.domain.db

import androidx.room.*
import com.keralarecipemaster.user.domain.model.RecipeRequestEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeRequestsDao {
    @Query("SELECT * from recipe_requests")
    fun getAllRecipeRequests(): Flow<List<RecipeRequestEntity>>

    @Query("DELETE from recipe_requests")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeRequest(recipeRequest: RecipeRequestEntity)

    @Delete
    suspend fun deleteRecipeRequest(recipe: RecipeRequestEntity)

    @Query("SELECT * from recipe_requests WHERE recipeId = :requestId")
    fun getRecipeRequestDetails(requestId: Int): Flow<RecipeRequestEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecipe(recipeRequest: RecipeRequestEntity)

    @Query("DELETE from recipe_requests WHERE recipeId = :recipeId")
    suspend fun deleteRecipe(recipeId: Int)
}
