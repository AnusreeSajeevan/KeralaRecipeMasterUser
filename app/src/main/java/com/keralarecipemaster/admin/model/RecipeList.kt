package com.keralarecipemaster.admin.model

import com.google.gson.annotations.SerializedName

data class RecipeList(
    @SerializedName("default_recipes")
    val defaultRecipes: List<Recipe>
)
