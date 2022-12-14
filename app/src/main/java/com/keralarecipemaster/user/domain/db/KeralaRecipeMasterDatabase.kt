package com.keralarecipemaster.user.domain.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.keralarecipemaster.user.domain.model.FamousRestaurant
import com.keralarecipemaster.user.domain.model.RecipeEntity
import com.keralarecipemaster.user.domain.model.RecipeRequestEntity
import com.keralarecipemaster.user.utils.*
import kotlinx.coroutines.CoroutineScope

@Database(
    entities = [RecipeEntity::class, RecipeRequestEntity::class, FamousRestaurant::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    DietTypeConverter::class,
    MealsTypeConverter::class,
    IngredientsTypeConverter::class
)
abstract class KeralaRecipeMasterDatabase : RoomDatabase() {
    abstract fun getRecipeDao(): RecipeDao
    abstract fun getRecipeRequestDao(): RecipeRequestsDao
    abstract fun getFamousLocationDao(): FamousLocationDao

    companion object {
        @Volatile
        private var INSTANCE: KeralaRecipeMasterDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): KeralaRecipeMasterDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KeralaRecipeMasterDatabase::class.java,
                    "kerala_recipe_master_database"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
