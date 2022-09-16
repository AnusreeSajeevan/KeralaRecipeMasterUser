package com.keralarecipemaster.user.domain.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.keralarecipemaster.user.domain.model.Ingredient
import com.keralarecipemaster.user.domain.model.RecipeEntity
import com.keralarecipemaster.user.domain.model.RecipeRequestEntity
import com.keralarecipemaster.user.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [RecipeEntity::class, RecipeRequestEntity::class], version = 1, exportSchema = false)
@TypeConverters(
    DietTypeConverter::class,
    MealsTypeConverter::class,
    IngredientsTypeConverter::class
)
abstract class RecipeMemoriesDatabase : RoomDatabase() {
    abstract fun getRecipeDao(): RecipeDao
    abstract fun getRecipeRequestDao(): RecipeRequestsDao

    class RecipeDatabaseCallback(private val scope: CoroutineScope) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
           /* INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.getRecipeDao())
                }
            }*/
        }

        private suspend fun populateDatabase(
            recipeDao: RecipeDao
        ) {
            recipeDao.deleteAll()
            populateRecipes(recipeDao)
        }

        private suspend fun populateRecipes(recipeDao: RecipeDao) {
            insertRecipe(
                recipeDao,
                id = 1,
                recipeName = "Vegetable biriyani",
                description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
                ingredients = arrayListOf(
                    Ingredient("carrot", "2"),
                    Ingredient("jira rice", "1 kg"),
                    Ingredient("salt", "2 tspoon")
                ),
                preparationMethod = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. ",
                imageUrl = "image url",
                meal = Meal.LUNCH,
                diet = Diet.VEG,
                rating = 2,
                userType = UserType.USER
            )

            insertRecipe(
                recipeDao,
                id = 2,
                rating = 5,
                recipeName = "Nirvana",
                description = "Sepcial",
                ingredients = arrayListOf(
                    Ingredient("fish", "5"),
                    Ingredient("milk", "2 cup"),
                    Ingredient("pepper", "3 tblspoon")
                ),
                preparationMethod = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
                imageUrl = "image url",
                meal = Meal.DINNER,
                diet = Diet.NON_VEG,
                restaurantName = "Restaurant Chef Pillai",
                latitude = "latitude",
                longitude = "longitude",
                address = "Kerala",
                userType = UserType.RESTAURANT
            )
        }

        private suspend fun insertRecipe(
            recipeDao: RecipeDao,
            id: Int,
            rating: Int,
            recipeName: String,
            description: String,
            ingredients: ArrayList<Ingredient>,
            preparationMethod: String,
            imageUrl: String,
            meal: Meal,
            diet: Diet,
            restaurantName: String = Constants.EMPTY_STRING,
            latitude: String = Constants.EMPTY_STRING,
            longitude: String = Constants.EMPTY_STRING,
            address: String = Constants.EMPTY_STRING,
            userType: UserType
        ) {
            recipeDao.insertRecipe(
                RecipeEntity(
                    id = id,
                    rating = rating,
                    recipeName = recipeName,
                    description = description,
                    ingredients = ingredients,
                    preparationMethod = preparationMethod,
                    image = imageUrl,
                    restaurantName = restaurantName,
                    restaurantLatitude = latitude,
                    restaurantLongitude = longitude,
                    restaurantAddress = address,
                    mealType = meal,
                    diet = diet,
                    addedBy = userType
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
