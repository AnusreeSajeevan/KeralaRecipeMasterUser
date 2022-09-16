package com.keralarecipemaster.user.network.model.reciperequest

import com.google.gson.annotations.SerializedName
import com.keralarecipemaster.user.domain.model.Ingredient
import com.keralarecipemaster.user.domain.model.Restaurant

data class RecipeRequestResponse(
    @SerializedName("request_id")
    val requestId: Int,

    @SerializedName("recipe_name")
    val recipeName: String,

    val description: String,

    val ingredients: List<Ingredient>,

    val image: String? = null,

    val restaurant: Restaurant,

    @SerializedName("preparation_method")
    val preparationMethod: String,

    @SerializedName("meal_category")
    val mealType: String,

    @SerializedName("diet_category")
    val diet: String,

    val rating: Int
)
