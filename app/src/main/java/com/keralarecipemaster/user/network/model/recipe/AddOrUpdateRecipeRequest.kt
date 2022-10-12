package com.keralarecipemaster.user.network.model.recipe

import com.google.gson.annotations.SerializedName

data class AddOrUpdateRecipeRequest(
    @SerializedName("user_id") val userId: Int,
    val recipe: RecipeResponse
)
