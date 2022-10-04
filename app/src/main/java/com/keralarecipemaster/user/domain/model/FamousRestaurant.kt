package com.keralarecipemaster.user.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "famous_restaurant")
data class FamousRestaurant(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val restaurantAddress: String,
    val restaurantName: String,
    val latitude: String,
    val longitude: String
)
