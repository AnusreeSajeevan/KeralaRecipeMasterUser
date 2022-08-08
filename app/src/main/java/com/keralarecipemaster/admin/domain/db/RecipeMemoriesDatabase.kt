package com.keralarecipemaster.admin.domain.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.keralarecipemaster.admin.domain.model.Recipe
import com.keralarecipemaster.admin.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Recipe::class], version = 1, exportSchema = false)
@TypeConverters(
    DietTypeConverter::class,
    MealsTypeConverter::class,
    IngredientsTypeConverter::class
)
abstract class RecipeMemoriesDatabase : RoomDatabase() {
    abstract fun getRecipeDao(): RecipeDao

    class RecipeDatabaseCallback(private val scope: CoroutineScope) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.getRecipeDao())
                }
            }
        }

        private suspend fun populateDatabase(recipeDao: RecipeDao) {
            recipeDao.deleteAll()
            insertRecipe(
                recipeDao,
                1,
                "Nadan Chicken Curry",
                "This is a tasty nadan chicken curry",
                listOf("chicken", "jira rice", "spices"),
                "Prepare like this",
                "image url",
                Meal.LUNCH,
                Diet.NON_VEG,
                "Thalassery Restaurant",
                "latitude",
                "longitude",
                "Kerala"
            )

            insertRecipe(
                recipeDao,
                2,
                "Vegetable Biriyani",
                "Veg biriyani",
                listOf("greenps", "jira rice", "spices"),
                "Prepare like this",
                "image url",
                Meal.DINNER,
                Diet.VEG,
                "Thalassery Restaurant",
                "latitude",
                "longitude",
                "Kerala"
            )
        }

        private suspend fun insertRecipe(
            recipeDao: RecipeDao,
            id: Int,
            recipeName: String,
            description: String,
            ingredients: List<String>,
            preparationMethod: String,
            imageUrl: String,
            meal: Meal,
            diet: Diet,
            restaurantName: String,
            latitude: String,
            longitude: String,
            state: String
        ) {
            recipeDao.insertRecipe(
                Recipe(
                    id = id,
                    recipeName = recipeName,
                    description = description,
                    ingredients = ingredients,
                    preparationMethod = preparationMethod,
                    image = imageUrl,
                    restaurantName = restaurantName,
                    restaurantLatitude = latitude,
                    restaurantLongitude = longitude,
                    restaurantState = state,
                    mealType = meal,
                    diet = diet
                )
            )
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: RecipeMemoriesDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): RecipeMemoriesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecipeMemoriesDatabase::class.java,
                    "recipe_memories_database"
                ).addCallback(RecipeDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
