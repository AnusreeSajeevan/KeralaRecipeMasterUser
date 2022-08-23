package com.keralarecipemaster.admin.network.model

import com.google.gson.annotations.SerializedName
import com.keralarecipemaster.admin.domain.model.Ingredient
import com.keralarecipemaster.admin.domain.model.Restaurant

data class RecipeResponse(
    val id: Int,

    @SerializedName("recipe_name")
    val recipeName: String,

    val description: String,

    val ingredients: List<Ingredient>,

    val image: String? = null,

    val restaurant: Restaurant,

    @SerializedName("preparation_method")
    val preparationMethod: String,

    @SerializedName("meal_type")
    val mealType: String,

    val diet: String,

    val rating: Int
)
