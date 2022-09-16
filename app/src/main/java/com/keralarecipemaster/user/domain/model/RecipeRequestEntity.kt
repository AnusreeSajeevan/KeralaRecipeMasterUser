package com.keralarecipemaster.user.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.keralarecipemaster.user.utils.Diet
import com.keralarecipemaster.user.utils.Meal

@Entity(tableName = "recipe_requests")
data class RecipeRequestEntity(
    @PrimaryKey(autoGenerate = true)
    val requestId: Int,
    val recipeName: String,
    val description: String,
    val ingredients: List<Ingredient>,
    val image: String? = null,
    val restaurantName: String,
    val restaurantLatitude: String,
    val restaurantLongitude: String,
    val restaurantAddress: String,
    val preparationMethod: String,
    val mealType: Meal,
    val diet: Diet,
    val rating: Int
)
