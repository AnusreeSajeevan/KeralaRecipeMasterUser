package com.keralarecipemaster.admin.domain.model

import com.google.gson.annotations.SerializedName
import com.keralarecipemaster.admin.network.model.RecipeResponse

data class RecipeResponseWrapper(
    @SerializedName("recipes")
    val defaultRecipes: List<RecipeResponse>
)
