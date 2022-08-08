package com.keralarecipemaster.admin.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.keralarecipemaster.admin.utils.Diet
import com.keralarecipemaster.admin.utils.Meal

@Entity(tableName = "recipe")
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val recipeName: String,
    val description: String,
    val ingredients: List<String>,
    val image: String? = null,
    val restaurantName: String,
    val restaurantLatitude: String,
    val restaurantLongitude: String,
    val restaurantState: String,
    val preparationMethod: String,
    val mealType: Meal,
    val diet: Diet
)
