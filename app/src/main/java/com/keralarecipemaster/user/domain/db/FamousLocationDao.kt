package com.keralarecipemaster.user.domain.db

import androidx.room.*
import com.keralarecipemaster.user.domain.model.FamousRestaurant
import kotlinx.coroutines.flow.Flow

@Dao
interface FamousLocationDao {
    @Query("SELECT * from famous_restaurant")
    fun getAllFamousLocations(): Flow<List<FamousRestaurant>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFamousLocation(famousLocation: FamousRestaurant)

    @Query("SELECT COUNT(restaurantName) from famous_restaurant")
    fun numberOfLocations(): Int
}
