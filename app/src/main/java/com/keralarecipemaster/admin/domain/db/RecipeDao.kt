package com.keralarecipemaster.admin.domain.db

import androidx.room.*
import com.keralarecipemaster.admin.domain.model.Recipe

@Dao
interface RecipeDao {
    @Query("SELECT * from recipe")
    fun getAllRecipes(): List<Recipe>

    @Query("DELETE from recipe")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecipe(recipe: Recipe)

    @Query("SELECT * from recipe WHERE recipeName = :recipeName")
    suspend fun getRecipeDetails(recipeName: String): Recipe
}
