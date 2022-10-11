package com.keralarecipemaster.user.network.model.reciperequest

import com.google.gson.annotations.SerializedName
import com.keralarecipemaster.user.domain.model.Ingredient
import com.keralarecipemaster.user.domain.model.Restaurant
import com.keralarecipemaster.user.utils.Constants

data class RecipeRequestResponse(
    @SerializedName("recipe_id")
    val id: Int,

    @SerializedName("recipe_name")
    val recipeName: String,

    val description: String,

    val ingredients: List<Ingredient>,

    val image: String = Constants.EMPTY_STRING,
    @SerializedName("image_name") val imageName: String = Constants.EMPTY_STRING,

    val restaurant: Restaurant,

    @SerializedName("preparation_method")
    val preparationMethod: String,

    @SerializedName("meal_category")
    val mealType: String,

    @SerializedName("diet_category")
    val diet: String,

    val rating: Int,

    @SerializedName("added_by")
    val addedBy: String,

    val status: String
)
