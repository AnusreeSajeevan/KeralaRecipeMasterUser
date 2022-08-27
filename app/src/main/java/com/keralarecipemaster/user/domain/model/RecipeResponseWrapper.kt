package com.keralarecipemaster.user.domain.model

import com.google.gson.annotations.SerializedName
import com.keralarecipemaster.user.network.model.RecipeResponse

data class RecipeResponseWrapper(
    @SerializedName("recipes")
    val defaultRecipes: List<RecipeResponse>
)
