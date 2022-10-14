package com.keralarecipemaster.user.repository

import com.keralarecipemaster.user.domain.model.FamousRestaurant
import kotlinx.coroutines.flow.Flow

interface FamousLocationRepository {
    suspend fun getAllFamousLocations(): Flow<List<FamousRestaurant>>
}