package com.keralarecipemaster.user.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "famous_location")
data class FamousLocation(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val address: String,
    val latitude: String,
    val longitude: String
)
