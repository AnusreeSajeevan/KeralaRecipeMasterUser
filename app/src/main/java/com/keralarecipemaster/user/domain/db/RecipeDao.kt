package com.keralarecipemaster.user.domain.db

import androidx.room.*
import com.keralarecipemaster.user.domain.model.Ingredient
import com.keralarecipemaster.user.domain.model.RecipeEntity
import com.keralarecipemaster.user.utils.Diet
import com.keralarecipemaster.user.utils.Meal
import com.keralarecipemaster.user.utils.UserType
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * from recipe WHERE addedBy = :addedBy")
    fun getAllFamousRecipes(addedBy: String = UserType.OWNER.name): Flow<List<RecipeEntity>>

    @Query("SELECT * from recipe WHERE addedBy = :addedBy")
    fun getAllUserAddedRecipes(addedBy: String = UserType.USER.name): Flow<List<RecipeEntity>>

    @Query("DELETE from recipe")
    fun deleteAll()

    @Query("DELETE from recipe WHERE addedBy = :addedBy")
    fun deleteAllFamousRecipes(addedBy: String = UserType.OWNER.name)

    @Query("DELETE from recipe WHERE addedBy = :addedBy")
    fun deleteAllMyRecipes(addedBy: String = UserType.USER.name)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Query("UPDATE recipe SET recipeName=:recipeName, description=:description, diet=:diet, mealType=:meal,ingredients=:ingredients, rating=:rating, preparationMethod=:preparationMethod  WHERE id = :id")
    fun updateRecipe(
        id: Int,
        recipeName: String,
        description: String,
        diet: Diet,
        meal: Meal,
        ingredients: List<Ingredient>,
        preparationMethod: String,
        rating: Int
    )

    @Query("SELECT * from recipe WHERE id = :recipeId")
    fun getRecipeDetails(recipeId: Int): Flow<RecipeEntity>

    @Query("SELECT COUNT(recipeName) from recipe")
    fun numberOfRecipes(): Int

    @Query("DELETE from recipe WHERE id = :recipeId")
    suspend fun deleteRecipe(recipeId: Int)

    @Query("SELECT * from recipe WHERE recipeName LIKE '%' || :queryString || '%' AND addedBy =:addedBy")
    fun search(queryString: String, addedBy: String): Flow<List<RecipeEntity>>

    @Query("SELECT * from recipe WHERE diet = :diet AND addedBy = :userType")
    fun filterByDietType(diet: String, userType: UserType): Flow<List<RecipeEntity>>

    @Query("SELECT * from recipe WHERE diet = :diet AND addedBy = :userType")
    fun filterRestaurantAddedRecipesByDietType(
        diet: String,
        userType: UserType = UserType.OWNER
    ): Flow<List<RecipeEntity>>

    @Query("SELECT * from recipe WHERE diet = :diet AND addedBy = :userType")
    fun filterUserAddedRecipesByDietType(
        diet: String,
        userType: UserType = UserType.USER
    ): Flow<List<RecipeEntity>>
}
