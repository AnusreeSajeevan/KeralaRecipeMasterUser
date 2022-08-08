package com.keralarecipemaster.admin.domain.model

import com.google.gson.annotations.SerializedName
import com.keralarecipemaster.admin.network.model.RecipeDto

data class RecipeDtoWrapper(
    @SerializedName("default_recipes")
    val defaultRecipes: List<RecipeDto>
)
