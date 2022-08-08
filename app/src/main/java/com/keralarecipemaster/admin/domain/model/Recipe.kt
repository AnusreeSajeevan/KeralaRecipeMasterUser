package com.keralarecipemaster.admin.domain.model

import com.keralarecipemaster.admin.utils.Diet
import com.keralarecipemaster.admin.utils.Meal

data class Recipe(
    val recipeName: String,
    val description: String,
    val ingredients: List<String>,
    val image: String? = null,
    val restaurant: Restaurant,
    val preparationMethod: String,
    val mealType: Meal,
    val diet: Diet
)
