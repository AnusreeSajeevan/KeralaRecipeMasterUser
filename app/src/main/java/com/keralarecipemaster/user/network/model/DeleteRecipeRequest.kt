package com.keralarecipemaster.admin.network.model

import com.google.gson.annotations.SerializedName

data class DeleteRecipeRequest(
    @SerializedName("recipe_id")
    val recipeId: Int
)