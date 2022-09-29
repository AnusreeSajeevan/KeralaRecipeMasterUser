package com.keralarecipemaster.user.domain.model

import com.google.gson.annotations.SerializedName
import com.keralarecipemaster.user.network.model.recipe.RecipeResponse

data class RecipeResponseWrapper(
    @SerializedName("recipe_list")
    val recipes: List<RecipeResponse>
)
