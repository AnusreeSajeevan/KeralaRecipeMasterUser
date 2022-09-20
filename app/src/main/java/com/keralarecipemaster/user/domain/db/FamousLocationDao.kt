package com.keralarecipemaster.user.domain.db

import androidx.room.*
import com.keralarecipemaster.user.domain.model.FamousLocation
import kotlinx.coroutines.flow.Flow

@Dao
interface FamousLocationDao {
    @Query("SELECT * from famous_location")
    fun getAllFamousLocations(): Flow<List<FamousLocation>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFamousLocation(famousLocation: FamousLocation)

    @Query("SELECT COUNT(name) from famous_location")
    fun numberOfLocations(): Int
}
