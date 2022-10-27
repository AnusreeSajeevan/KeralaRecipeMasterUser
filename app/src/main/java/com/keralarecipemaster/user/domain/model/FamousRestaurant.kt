package com.keralarecipemaster.user.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "famous_restaurant")
data class FamousRestaurant(
    @PrimaryKey val id: Int,
    val restaurantAddress: String,
    val restaurantName: String,
    val latitude: Double,
    val longitude: Double
)
