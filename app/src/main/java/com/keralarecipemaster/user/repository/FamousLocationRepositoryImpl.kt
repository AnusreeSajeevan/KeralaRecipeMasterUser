package com.keralarecipemaster.user.repository

import com.keralarecipemaster.user.domain.db.FamousLocationDao
import com.keralarecipemaster.user.domain.model.FamousRestaurant
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FamousLocationRepositoryImpl @Inject constructor(private val famousLocationDao: FamousLocationDao): FamousLocationRepository {
    override suspend fun getAllFamousLocations(): Flow<List<FamousRestaurant>> {
        return famousLocationDao.getAllFamousLocations()
    }
}