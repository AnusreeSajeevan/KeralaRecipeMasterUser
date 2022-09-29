package com.keralarecipemaster.user.domain.model

import com.google.gson.annotations.SerializedName
import com.keralarecipemaster.user.network.model.recipe.RecipeResponse
import com.keralarecipemaster.user.network.model.reciperequest.RecipeRequestResponse

data class RecipeRequestResponseWrapper(
    @SerializedName("recipe_list")
    val recipeRequests: List<RecipeRequestResponse>
)
