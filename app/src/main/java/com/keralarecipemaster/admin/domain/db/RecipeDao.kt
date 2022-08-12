package com.keralarecipemaster.admin.domain.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.keralarecipemaster.admin.domain.model.Recipe
import com.keralarecipemaster.admin.utils.UserType
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * from recipe WHERE addedBy = :addedBy")
    fun getAllDefaultRecipes(addedBy: String = UserType.ADMIN.name): Flow<List<Recipe>>

    @Query("SELECT * from recipe WHERE addedBy = :addedBy")
    fun getAllUserAddedRecipes(addedBy: String = UserType.USER.name): LiveData<List<Recipe>>

    @Query("DELETE from recipe")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecipe(recipe: Recipe)

    @Query("SELECT * from recipe WHERE recipeName = :recipeName")
    suspend fun getRecipeDetails(recipeName: String): Recipe

    @Query("SELECT COUNT(recipeName) from recipe")
    fun numberOfRecipes(): Int

    @Delete
    suspend fun deleteRecipe(recipe: Recipe)

    @Query("SELECT * from recipe WHERE recipeName = :queryString")
    fun search(queryString: String): Flow<List<Recipe>>
}
