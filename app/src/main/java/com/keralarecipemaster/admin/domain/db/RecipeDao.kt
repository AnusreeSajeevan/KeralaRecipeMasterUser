package com.keralarecipemaster.admin.domain.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.keralarecipemaster.admin.domain.model.RecipeEntity
import com.keralarecipemaster.admin.utils.UserType
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * from recipe WHERE addedBy = :addedBy")
    fun getAllDefaultRecipes(addedBy: String = UserType.ADMIN.name): Flow<List<RecipeEntity>>

    @Query("SELECT * from recipe WHERE addedBy = :addedBy")
    fun getAllUserAddedRecipes(addedBy: String = UserType.USER.name): LiveData<List<RecipeEntity>>

    @Query("DELETE from recipe")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Query("SELECT * from recipe WHERE recipeName = :recipeName")
    suspend fun getRecipeDetails(recipeName: String): RecipeEntity

    @Query("SELECT COUNT(recipeName) from recipe")
    fun numberOfRecipes(): Int

    @Delete
    suspend fun deleteRecipe(recipe: RecipeEntity)

    @Query("SELECT * from recipe WHERE recipeName LIKE '%' || :queryString || '%' AND addedBy LIKE :addedBy")
    fun search(queryString: String, addedBy: String): Flow<List<RecipeEntity>>
}
