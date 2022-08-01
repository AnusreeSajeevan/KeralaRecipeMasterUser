package com.keralarecipemaster.admin.model

import com.google.gson.annotations.SerializedName
import com.keralarecipemaster.admin.utils.Diet
import com.keralarecipemaster.admin.utils.Meal

data class Recipe(
    @SerializedName("recipe_name")
    val recipeName: String,
    val image: String? = null,
    val restaurant: Restaurant,
    val preparationMethod: String,
    val mealType: Meal,
    val diet: Diet
)
