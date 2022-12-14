package com.keralarecipemaster.user.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.keralarecipemaster.user.utils.Constants
import com.keralarecipemaster.user.utils.Diet
import com.keralarecipemaster.user.utils.Meal
import com.keralarecipemaster.user.utils.UserType

@Entity(tableName = "recipe")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val recipeName: String,
    val description: String,
    val ingredients: List<Ingredient>,
    val image: String = Constants.EMPTY_STRING,
    val imageName: String = Constants.EMPTY_STRING,
    val restaurantName: String,
    val restaurantLatitude: String,
    val restaurantLongitude: String,
    val restaurantAddress: String,
    val preparationMethod: String,
    val mealType: Meal,
    val diet: Diet,
    val addedBy: UserType,
    val rating: Int,
    val status: String
)
