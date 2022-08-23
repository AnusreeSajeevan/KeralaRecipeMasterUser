package com.keralarecipemaster.admin.domain.db

import androidx.room.*
import com.keralarecipemaster.admin.domain.model.Ingredient
import com.keralarecipemaster.admin.domain.model.RecipeEntity
import com.keralarecipemaster.admin.utils.Diet
import com.keralarecipemaster.admin.utils.Meal
import com.keralarecipemaster.admin.utils.UserType
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * from recipe WHERE addedBy = :addedBy")
    fun getAllDefaultRecipes(addedBy: String = UserType.ADMIN.name): Flow<List<RecipeEntity>>

    @Query("SELECT * from recipe WHERE addedBy = :addedBy")
    fun getAllUserAddedRecipes(addedBy: String = UserType.USER.name): Flow<List<RecipeEntity>>

    @Query("DELETE from recipe")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Query("UPDATE recipe SET recipeName=:recipeName, description=:description, diet=:diet, mealType=:meal,ingredients=:ingredients, preparationMethod=:preparationMethod  WHERE id = :id")
    fun updateRecipe(
        id: Int,
        recipeName: String,
        description: String,
        diet: Diet,
        meal: Meal,
        ingredients: List<Ingredient>,
        preparationMethod: String
    )

    @Query("SELECT * from recipe WHERE id = :recipeId")
    fun getRecipeDetails(recipeId: Int): Flow<RecipeEntity>

    @Query("SELECT COUNT(recipeName) from recipe")
    fun numberOfRecipes(): Int

    @Delete
    suspend fun deleteRecipe(recipe: RecipeEntity)

    @Query("SELECT * from recipe WHERE recipeName LIKE '%' || :queryString || '%' AND addedBy =:addedBy")
    fun search(queryString: String, addedBy: String): Flow<List<RecipeEntity>>

    @Query("SELECT * from recipe WHERE diet = :diet AND addedBy = :userType")
    fun filterByDietType(diet: String, userType: UserType): Flow<List<RecipeEntity>>

    @Query("SELECT * from recipe WHERE diet = :diet AND addedBy = :userType")
    fun filterDefaultRecipesByDietType(
        diet: String,
        userType: UserType = UserType.ADMIN
    ): Flow<List<RecipeEntity>>

    @Query("SELECT * from recipe WHERE diet = :diet AND addedBy = :userType")
    fun filterUserAddedRecipesByDietType(
        diet: String,
        userType: UserType = UserType.USER
    ): Flow<List<RecipeEntity>>
}
